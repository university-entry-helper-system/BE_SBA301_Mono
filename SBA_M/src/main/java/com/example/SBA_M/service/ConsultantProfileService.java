package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ConsultantProfileRequest;
import com.example.SBA_M.dto.response.ConsultantProfileResponse;
import com.example.SBA_M.utils.StatusConsultant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ConsultantProfileService {

    ConsultantProfileResponse create(ConsultantProfileRequest request);

    ConsultantProfileResponse update(Integer id, ConsultantProfileRequest request);

    void delete(Integer id);

    ConsultantProfileResponse getById(Integer id);

    Page<ConsultantProfileResponse> getAllOnlineConsultants(Pageable pageable);

    Page<ConsultantProfileResponse> search(String keyword, Pageable pageable);

    public void changeConsultantStatus(Integer consultantProfileId, StatusConsultant newStatus);

    Page<ConsultantProfileResponse> getAll(Pageable pageable);
}
