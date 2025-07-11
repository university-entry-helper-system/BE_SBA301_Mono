package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ConsultantProfileRequest;
import com.example.SBA_M.dto.response.ConsultantProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ConsultantProfileService {

    ConsultantProfileResponse create(ConsultantProfileRequest request);

    ConsultantProfileResponse update(UUID id, ConsultantProfileRequest request);

    void delete(UUID id);

    ConsultantProfileResponse getById(UUID id);

    Page<ConsultantProfileResponse> getAll(Pageable pageable);

    Page<ConsultantProfileResponse> search(String keyword, Pageable pageable);
}
