package com.example.SBA_M.dto.request;

import lombok.Data;

@Data
public class GraduationScoreRequest {
    // Exam scores
    private Double literatureScore;
    private Double mathScore;
    private Double foreignLanguageScore; // null if exempted
    private Double electiveScore;

    private boolean isExemptedFromForeignLanguage;
    private double bonusScore;
    private double priorityScore;

    // Yearly subject scores
    private AllSubjectsScoreRequest allSubjectsScore;
}
