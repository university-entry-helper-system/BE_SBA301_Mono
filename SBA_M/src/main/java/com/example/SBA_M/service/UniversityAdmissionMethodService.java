package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityMethodRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodResponse;

public interface UniversityAdmissionMethodService {
    PageResponse<UniversityAdmissionMethodResponse> getAll(int page, int size);

    UniversityAdmissionMethodResponse getById(Integer id);

    UniversityAdmissionMethodResponse create(UniversityMethodRequest request);

    UniversityAdmissionMethodResponse update(Integer id, UniversityMethodRequest request);

    void delete(Integer id);
}
