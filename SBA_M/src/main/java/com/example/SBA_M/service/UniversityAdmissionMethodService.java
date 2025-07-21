package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityMethodRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodDetailResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodSummaryResponse;

import java.util.List;

public interface UniversityAdmissionMethodService {
    PageResponse<UniversityAdmissionMethodResponse> getAll(Integer universityId, int page, int size);

    UniversityAdmissionMethodResponse getById(Integer id);

    UniversityAdmissionMethodResponse create(UniversityMethodRequest request);

    UniversityAdmissionMethodResponse update(Integer id, UniversityMethodRequest request);

    void delete(Integer id);

    List<UniversityAdmissionMethodSummaryResponse> getSchoolsByMethod(Integer methodId);

    UniversityAdmissionMethodDetailResponse getMethodsBySchool(Integer universityId);
}
