package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ConsultationAnswerRequest;
import com.example.SBA_M.dto.request.ConsultationCreateRequest;
import com.example.SBA_M.dto.response.ConsultationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ConsultationService {
    ConsultationResponse createConsultation(ConsultationCreateRequest request);

    ConsultationResponse updateConsultation(Long consultationId, ConsultationCreateRequest request);


    Page<ConsultationResponse> getUserConsultations(UUID userId, Pageable pageable);

    Page<ConsultationResponse> searchUserConsultations(UUID userId, String keyword, Pageable pageable);

    ConsultationResponse answerConsultation(UUID consultantId, ConsultationAnswerRequest request);

    ConsultationResponse updateConsultantAnswer(UUID consultantId, ConsultationAnswerRequest request);

    void cancelConsultation(Long consultationId);

    Page<ConsultationResponse> getConsultantConsultations(UUID consultantId, Pageable pageable);

    Page<ConsultationResponse> searchConsultantConsultations(UUID consultantId, String keyword, Pageable pageable);
}
