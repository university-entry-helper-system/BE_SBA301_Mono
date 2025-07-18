package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ExamSubjectRequest;
import com.example.SBA_M.dto.response.ExamSubjectResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.Status;


public interface ExamSubjectService {

    ExamSubjectResponse createExamSubject(ExamSubjectRequest request);

    ExamSubjectResponse getExamSubjectById(Long id);

    PageResponse<ExamSubjectResponse> getAllExamSubjects(String search, int page, int size, String sort);

    ExamSubjectResponse updateExamSubject(Long id, ExamSubjectRequest request);

    void deleteExamSubject(Long id);

    boolean existsByName(String name);

    ExamSubjectResponse updateExamSubjectStatus(Long id, Status status);
}