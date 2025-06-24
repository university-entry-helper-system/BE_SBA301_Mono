package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ExamSubjectRequest;
import com.example.SBA_M.dto.response.ExamSubjectResponse;

import java.util.List;

public interface ExamSubjectService {

    ExamSubjectResponse createExamSubject(ExamSubjectRequest request);

    ExamSubjectResponse getExamSubjectById(Long id);

    List<ExamSubjectResponse> getAllExamSubjects();

    ExamSubjectResponse updateExamSubject(Long id, ExamSubjectRequest request);

    void deleteExamSubject(Long id);

    boolean existsByName(String name);
}