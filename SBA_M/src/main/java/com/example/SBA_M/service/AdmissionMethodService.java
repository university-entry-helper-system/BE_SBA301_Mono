package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.PageResponse;

import java.util.List;

public interface AdmissionMethodService {
    AdmissionMethodResponse createAdmissionMethod(AdmissionMethodRequest request, String username);
    PageResponse<AdmissionMethodResponse> getAllAdmissionMethods(int page, int size);
    AdmissionMethodResponse getAdmissionMethodById(Integer id);
    AdmissionMethodResponse updateAdmissionMethod(Integer id, AdmissionMethodRequest request, String username);
    void deleteAdmissionMethod(Integer id);
}
