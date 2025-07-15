package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AllSubjectsScoreRequest;
import com.example.SBA_M.dto.request.GraduationScoreRequest;
import com.example.SBA_M.dto.request.SubjectScoreByYearRequest;
import com.example.SBA_M.dto.response.GraduationScoreResponse;
import com.example.SBA_M.service.GraduationScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GraduationScoreServiceImpl implements GraduationScoreService {
    private static final double MIN_SUBJECT_SCORE = 1.5;
    private static final double PASSING_SCORE = 5.0;
    private static final int[] GRADES = {10, 11, 12};
    private static final double[] GRADE_WEIGHTS = {1.0, 2.0, 3.0};
    private static final String FAILURE_REASON = "Bạn bị điểm liệt trong kỳ thi";
    private static final String PASS_MESSAGE = "BẠN ĐÃ ĐỖ TỐT NGHIỆP THPT";
    private static final String FAIL_MESSAGE = "BẠN ĐÃ TRƯỢT TỐT NGHIỆP THPT";
    private static final int REQUIRED_MANDATORY_SUBJECTS = 5;
    private static final int REQUIRED_OPTIONAL_SUBJECTS = 2;

    @Override
    public GraduationScoreResponse calculateGraduationScore(GraduationScoreRequest request) {
        // Validate input
        validateRequest(request);

        // Check for failing score
        String reason = checkForFailingScore(request);

        // Calculate total exam score
        double totalExamScore = calculateTotalExamScore(request);
        int numberOfExamSubjects = request.isExemptedFromForeignLanguage() ? 3 : 4;
        double averageExamScore = (totalExamScore + request.getBonusScore()) / numberOfExamSubjects;

        // Calculate weighted school year average
        double averageSchoolScore = calculateWeightedSchoolAverage(request.getAllSubjectsScore());



        // Calculate final score
        double finalScore = round((averageExamScore + averageSchoolScore + request.getPriorityScore())/2);

        // Determine result
        String resultMessage = finalScore >= PASSING_SCORE ? PASS_MESSAGE : FAIL_MESSAGE;

        return new GraduationScoreResponse(
                round(totalExamScore),
                round(request.getBonusScore()),
                round(averageSchoolScore),
                round(request.getPriorityScore()),
                finalScore,
                resultMessage,
                reason
        );
    }

    private void validateRequest(GraduationScoreRequest request) {
        if (request == null || request.getAllSubjectsScore() == null) {
            throw new IllegalArgumentException("Request or subject scores cannot be null");
        }
    }

    private String checkForFailingScore(GraduationScoreRequest request) {
        if (request.getLiteratureScore() < MIN_SUBJECT_SCORE ||
                request.getMathScore() < MIN_SUBJECT_SCORE ||
                request.getElectiveScore() < MIN_SUBJECT_SCORE ||
                (!request.isExemptedFromForeignLanguage() &&
                        request.getForeignLanguageScore() != null &&
                        request.getForeignLanguageScore() < MIN_SUBJECT_SCORE)) {
            return FAILURE_REASON;
        }
        return null;
    }

    private double calculateTotalExamScore(GraduationScoreRequest request) {
        double total = request.getLiteratureScore() +
                request.getMathScore() +
                request.getElectiveScore();
        if (!request.isExemptedFromForeignLanguage() && request.getForeignLanguageScore() != null) {
            total += request.getForeignLanguageScore();
        }
        return total;
    }

    private double calculateWeightedSchoolAverage(AllSubjectsScoreRequest subjects) {
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (int i = 0; i < GRADES.length; i++) {
            double yearlyAverage = calculateYearlyAverage(subjects, GRADES[i]);
            weightedSum += yearlyAverage * GRADE_WEIGHTS[i];
            totalWeight += GRADE_WEIGHTS[i];
        }

        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    public double calculateYearlyAverage(AllSubjectsScoreRequest subjects, int grade) {
        List<SubjectScoreByYearRequest> allSubjects = Arrays.asList(
                subjects.getLiterature(), subjects.getMath(), subjects.getForeignLanguage(),
                subjects.getNationalDefense(), subjects.getHistory(),
                subjects.getChemistry(), subjects.getBiology(), subjects.getPhysics(),
                subjects.getGeography(), subjects.getCivicEducation(),
                subjects.getInformatics(), subjects.getTechnology()
        );

        // Validate mandatory subjects (Literature, Math, ForeignLanguage, NationalDefense, History)
        long mandatoryCount = Arrays.asList(
                subjects.getLiterature(), subjects.getMath(), subjects.getForeignLanguage(),
                subjects.getNationalDefense(), subjects.getHistory()
        ).stream().filter(Objects::nonNull).count();

        if (mandatoryCount != REQUIRED_MANDATORY_SUBJECTS) {
            throw new IllegalArgumentException("All mandatory subjects (Literature, Math, ForeignLanguage, NationalDefense, History) must be provided");
        }

        // Validate optional subjects (at least 2 of Chemistry, Biology, Physics, Geography, CivicEducation, Informatics, Technology)
        long optionalCount = Arrays.asList(
                subjects.getChemistry(), subjects.getBiology(), subjects.getPhysics(),
                subjects.getGeography(), subjects.getCivicEducation(),
                subjects.getInformatics(), subjects.getTechnology()
        ).stream().filter(Objects::nonNull).count();

        if (optionalCount < REQUIRED_OPTIONAL_SUBJECTS) {
            throw new IllegalArgumentException("At least two optional subjects (Chemistry, Biology, Physics, Geography, CivicEducation, Informatics, Technology) must be provided");
        }

        double total = 0.0;
        int count = 0;

        for (SubjectScoreByYearRequest subject : allSubjects) {
            if (subject == null) continue;

            Double score = switch (grade) {
                case 10 -> subject.getGrade10();
                case 11 -> subject.getGrade11();
                case 12 -> subject.getGrade12();
                default -> throw new IllegalArgumentException("Invalid grade: " + grade);
            };

            if (score != null) {
                total += score;
                count++;
            }
        }

        return count == 0 ? 0 : total / count;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
