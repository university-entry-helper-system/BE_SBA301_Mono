package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.MajorRequest;
import com.example.SBA_M.dto.response.MajorResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface MajorService {
    MajorResponse createMajor(MajorRequest request);
    MajorResponse getMajorById(Long id);
    PageResponse<MajorResponse> getAllMajors(String search, int page, int size, String sort);
    MajorResponse updateMajor(Long id, MajorRequest request);
    void deleteMajor(Long id);
    MajorResponse updateMajorStatus(Long id, Status status);
}