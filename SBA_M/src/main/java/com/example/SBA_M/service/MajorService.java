package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.MajorRequest;
import com.example.SBA_M.dto.response.MajorResponse;

import java.util.List;

public interface MajorService {
    MajorResponse createMajor(MajorRequest request);
    MajorResponse getMajorById(Long id);
    List<MajorResponse> getAllMajors();
    MajorResponse updateMajor(Long id, MajorRequest request);
    void deleteMajor(Long id);
}