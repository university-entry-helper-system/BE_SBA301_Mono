package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.ExamSubjectResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.entity.commands.ExamSubject;
import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.ExamSubjectRepository;
import com.example.SBA_M.repository.commands.SubjectCombinationRepository;
import com.example.SBA_M.service.SubjectCombinationService;
import com.example.SBA_M.service.messaging.producer.SubjectCombinationProduce;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectCombinationServiceImpl implements SubjectCombinationService {

    private final SubjectCombinationRepository subjectCombinationRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final SubjectCombinationProduce subjectCombinationProduce;

    @Override
    @Transactional
    public SubjectCombinationResponse createSubjectCombination(SubjectCombinationRequest request) {
        log.info("Creating new subject combination with name: {}", request.getName());

        // Map request to entity
        SubjectCombination subjectCombination = new SubjectCombination();
        subjectCombination.setName(request.getName());
        subjectCombination.setDescription(request.getDescription());
        subjectCombination.setStatus(Status.ACTIVE);

        // Find and set exam subjects
        List<ExamSubject> examSubjects = findExamSubjects(request.getExamSubjectIds());
        subjectCombination.setExamSubjects(examSubjects);

        // Save to database
        SubjectCombination savedCombination = subjectCombinationRepository.save(subjectCombination);
        log.info("Subject combination created with ID: {}", savedCombination.getId());

        // Map to response
        return mapToResponse(savedCombination);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectCombinationResponse getSubjectCombinationById(Long id) {
        log.info("Fetching subject combination with ID: {}", id);

        SubjectCombination subjectCombination = findSubjectCombinationById(id);
        return mapToResponse(subjectCombination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectCombinationResponse> getAllSubjectCombinations() {
        log.info("Fetching all subject combinations");

        List<SubjectCombination> combinations = subjectCombinationRepository.findAll();
        return combinations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubjectCombinationResponse updateSubjectCombination(Long id, SubjectCombinationRequest request) {
        log.info("Updating subject combination with ID: {}", id);

        SubjectCombination existingCombination = findSubjectCombinationById(id);

        // Update fields
        existingCombination.setName(request.getName());
        existingCombination.setDescription(request.getDescription());

        // Update exam subjects
        List<ExamSubject> examSubjects = findExamSubjects(request.getExamSubjectIds());
        existingCombination.setExamSubjects(examSubjects);

        // Save changes
        existingCombination.setStatus(Status.ACTIVE);
        subjectCombinationProduce.sendSubjectCombinationUpdatedEvent(id, existingCombination.getName());
        SubjectCombination updatedCombination = subjectCombinationRepository.save(existingCombination);
        log.info("Subject combination updated with ID: {}", updatedCombination.getId());

        // Map to response
        return mapToResponse(updatedCombination);
    }

    @Override
    @Transactional
    public void deleteSubjectCombination(Long id) {
        log.info("Deleting subject combination with ID: {}", id);

        SubjectCombination combination = findSubjectCombinationById(id);
        combination.setStatus(Status.DELETED);
        subjectCombinationRepository.save(combination);
        subjectCombinationProduce.sendSubjectCombinationDeletedEvent(id);
        log.info("Subject combination deleted with ID: {}", id);
    }

    // Helper methods
    private SubjectCombination findSubjectCombinationById(Long id) {
        return subjectCombinationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_COMBINATION_NOT_FOUND));
    }

    private List<ExamSubject> findExamSubjects(List<Long> examSubjectIds) {
        if (examSubjectIds == null || examSubjectIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<ExamSubject> examSubjects = examSubjectRepository.findAllById(examSubjectIds);

        if (examSubjects.size() != examSubjectIds.size()) {
            throw new AppException(ErrorCode.EXAM_SUBJECT_NOT_FOUND);
        }

        return examSubjects;
    }

    private SubjectCombinationResponse mapToResponse(SubjectCombination combination) {
        return SubjectCombinationResponse.builder()
                .id(combination.getId())
                .name(combination.getName())
                .description(combination.getDescription())
                .examSubjects(combination.getExamSubjects().stream()
                        .map(subject -> ExamSubjectResponse.builder()
                                .id(subject.getId())
                                .name(subject.getName())
                                .shortName(subject.getShortName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}