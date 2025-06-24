package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;

import java.util.List;

public interface SubjectCombinationService {

    SubjectCombinationResponse createSubjectCombination(SubjectCombinationRequest request);


    SubjectCombinationResponse getSubjectCombinationById(Long id);


    List<SubjectCombinationResponse> getAllSubjectCombinations();


    SubjectCombinationResponse updateSubjectCombination(Long id, SubjectCombinationRequest request);


    void deleteSubjectCombination(Long id);
}