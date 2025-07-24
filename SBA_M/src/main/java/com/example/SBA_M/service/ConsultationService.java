package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ConsultationAnswerRequest;
import com.example.SBA_M.dto.request.ConsultationCreateRequest;
import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.dto.response.GroupedConsultationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ConsultationService {
    ConsultationResponse createConsultation(ConsultationCreateRequest request);

    ConsultationResponse updateConsultation(Long consultationId, ConsultationCreateRequest request);


    Page<ConsultationResponse> getUserConsultations(UUID consultantID ,Pageable pageable);

    Page<ConsultationResponse> searchUserConsultations(UUID consultantID, String keyword, Pageable pageable);

    ConsultationResponse answerConsultation(ConsultationAnswerRequest request);

    ConsultationResponse updateConsultantAnswer(ConsultationAnswerRequest request);

    void cancelConsultation(Long consultationId);

    Page<GroupedConsultationResponse> getConsultantConsultations(Pageable pageable);

    Page<ConsultationResponse> searchConsultantConsultations(String keyword, Pageable pageable);

    ConsultationResponse getConsultationById(Long consultationId);
}
