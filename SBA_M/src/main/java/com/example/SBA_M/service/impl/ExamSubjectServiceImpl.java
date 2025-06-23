package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.ExamSubjectRequest;
import com.example.SBA_M.dto.response.ExamSubjectResponse;
import com.example.SBA_M.entity.commands.ExamSubject;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.ExamSubjectRepository;
import com.example.SBA_M.service.ExamSubjectService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamSubjectServiceImpl implements ExamSubjectService {

    private final ExamSubjectRepository examSubjectRepository;

    @Override
    @Transactional
    public ExamSubjectResponse createExamSubject(ExamSubjectRequest request) {
        log.info("Creating new exam subject with name: {}", request.getName());

        ExamSubject examSubject = new ExamSubject();
        examSubject.setName(request.getName());
        examSubject.setShortName(request.getShortName());
        examSubject.setStatus(Status.ACTIVE);
        examSubject = examSubjectRepository.save(examSubject);
        log.info("Exam subject created with ID: {}", examSubject.getId());

        return mapToResponse(examSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSubjectResponse getExamSubjectById(Long id) {
        log.info("Fetching exam subject with ID: {}", id);

        ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SUBJECT_NOT_FOUND));

        return mapToResponse(examSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getAllExamSubjects() {
        log.info("Fetching all exam subjects");

        return examSubjectRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExamSubjectResponse updateExamSubject(Long id, ExamSubjectRequest request) {
        log.info("Updating exam subject with ID: {}", id);

        ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SUBJECT_NOT_FOUND));

        examSubject.setName(request.getName());
        examSubject.setShortName(request.getShortName());

        examSubject = examSubjectRepository.save(examSubject);
        log.info("Exam subject updated with ID: {}", examSubject.getId());

        return mapToResponse(examSubject);
    }

    @Override
    @Transactional
    public void deleteExamSubject(Long id) {
        log.info("Deleting exam subject with ID: {}", id);
        ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SUBJECT_NOT_FOUND));
      examSubject.setStatus(Status.DELETED);

        examSubjectRepository.save(examSubject);
        log.info("Exam subject deleted with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return examSubjectRepository.existsByName(name);
    }

    private ExamSubjectResponse mapToResponse(ExamSubject examSubject) {
        return ExamSubjectResponse.builder()
                .id(examSubject.getId())
                .name(examSubject.getName())
                .shortName(examSubject.getShortName())
                .build();
    }
}