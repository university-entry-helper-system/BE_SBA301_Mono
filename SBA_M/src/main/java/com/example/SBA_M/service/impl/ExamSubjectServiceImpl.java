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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.SBA_M.dto.response.PageResponse;

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
    public PageResponse<ExamSubjectResponse> getAllExamSubjects(String search, int page, int size, String sort) {
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<ExamSubject> subjectPage = examSubjectRepository.findByStatusAndNameContainingIgnoreCase(
            Status.ACTIVE,
            search != null ? search : "",
            pageable
        );
        List<ExamSubjectResponse> items = subjectPage.getContent().stream().map(this::mapToResponse).toList();
        return PageResponse.<ExamSubjectResponse>builder()
                .page(subjectPage.getNumber())
                .size(subjectPage.getSize())
                .totalElements(subjectPage.getTotalElements())
                .totalPages(subjectPage.getTotalPages())
                .items(items)
                .build();
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
    public ExamSubjectResponse updateExamSubjectStatus(Long id, Status status) {
        ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SUBJECT_NOT_FOUND));
        examSubject.setStatus(status);
        examSubject = examSubjectRepository.save(examSubject);
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
                .status(examSubject.getStatus())
                .build();
    }
}