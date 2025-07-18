package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface SubjectCombinationService {

    SubjectCombinationResponse createSubjectCombination(SubjectCombinationRequest request);


    SubjectCombinationResponse getSubjectCombinationById(Long id);


    PageResponse<SubjectCombinationResponse> getAllSubjectCombinations(String search, int page, int size, String sort, String block, String examSubject);


    SubjectCombinationResponse updateSubjectCombination(Long id, SubjectCombinationRequest request);


    void deleteSubjectCombination(Long id);

    SubjectCombinationResponse updateSubjectCombinationStatus(Long id, Status status);
}