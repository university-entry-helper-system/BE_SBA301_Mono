package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface AdmissionMethodService {
    AdmissionMethodResponse createAdmissionMethod(AdmissionMethodRequest request, String username);
    PageResponse<AdmissionMethodResponse> getAllAdmissionMethods(String search, int page, int size, String sort);
    AdmissionMethodResponse getAdmissionMethodById(Integer id);
    AdmissionMethodResponse updateAdmissionMethod(Integer id, AdmissionMethodRequest request, String username);
    void deleteAdmissionMethod(Integer id);
    AdmissionMethodResponse updateAdmissionMethodStatus(Integer id, Status status);
}
