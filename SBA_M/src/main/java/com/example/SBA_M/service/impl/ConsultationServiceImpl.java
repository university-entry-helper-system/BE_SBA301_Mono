package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.ConsultationAnswerRequest;
import com.example.SBA_M.dto.request.ConsultationCreateRequest;
import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Consultation;
import com.example.SBA_M.mapper.ConsultationMapper;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.ConsultationRepository;
import com.example.SBA_M.service.ConsultationService;
import com.example.SBA_M.utils.ConsultationStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AccountRepository accountRepository;
    private final ConsultationMapper consultationMapper;

    @Override
    @Transactional
    public ConsultationResponse createConsultation(ConsultationCreateRequest request) {
        Account sender = accountRepository.findById(request.getUser())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        Account consultant = accountRepository.findById(request.getConsultant())
                .orElseThrow(() -> new EntityNotFoundException("Consultant not found"));

        Consultation consultation = new Consultation();
        consultation.setAccount(sender);
        consultation.setConsultant(consultant);
        consultation.setTitle(request.getTitle());
        consultation.setContent(request.getContent());
        consultation.setConsultationsStatus(ConsultationStatus.PENDING);
        consultation.setSentAt(Instant.now());

        Consultation saved = consultationRepository.save(consultation);
        return consultationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ConsultationResponse updateConsultation(Long consultationId, ConsultationCreateRequest request) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        if (consultation.getConsultationsStatus() != ConsultationStatus.PENDING) {
            throw new IllegalStateException("Cannot update a consultation that is not pending.");
        }

        consultation.setTitle(request.getTitle());
        consultation.setContent(request.getContent());
        consultation.setSenderUpdatedAt(Instant.now());

        Consultation updated = consultationRepository.save(consultation);
        return consultationMapper.toResponse(updated);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationResponse> getUserConsultations(UUID userId, Pageable pageable) {
        return consultationRepository.findByAccountId(userId, pageable)
                .map(consultationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationResponse> searchUserConsultations(UUID userId, String keyword, Pageable pageable) {
        Specification<Consultation> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("account").get("id"), userId),
                cb.or(
                        cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%")
                )
        );

        return consultationRepository.findAll(spec, pageable)
                .map(consultationMapper::toResponse);
    }

    @Override
    @Transactional
    public ConsultationResponse answerConsultation(UUID consultantId, ConsultationAnswerRequest request) {
        Consultation consultation = consultationRepository.findById(request.getConsultationId())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        if (!consultation.getConsultant().getId().equals(consultantId)) {
            throw new IllegalArgumentException("Not your consultation.");
        }

        if (consultation.getConsultationsStatus() == ConsultationStatus.CANCELED) {
            throw new IllegalStateException("Cannot answer a canceled consultation.");
        }

        if (consultation.getConsultationsStatus() == ConsultationStatus.COMPLETED) {
            throw new IllegalStateException("Consultation already completed. Use update instead.");
        }

        applyConsultantAnswer(consultation, request, true);
        Consultation updated = consultationRepository.save(consultation);
        return consultationMapper.toResponse(updated);
    }


    @Override
    @Transactional
    public ConsultationResponse updateConsultantAnswer(UUID consultantId, ConsultationAnswerRequest request) {
        Consultation consultation = consultationRepository.findById(request.getConsultationId())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        if (!consultation.getConsultant().getId().equals(consultantId)) {
            throw new IllegalArgumentException("Not your consultation.");
        }

        if (consultation.getConsultationsStatus() == ConsultationStatus.CANCELED) {
            throw new IllegalStateException("Cannot update a canceled consultation.");
        }

        if (consultation.getConsultationsStatus() != ConsultationStatus.COMPLETED) {
            throw new IllegalStateException("Cannot update answer unless consultation is completed.");
        }

        applyConsultantAnswer(consultation, request, false);
        Consultation updated = consultationRepository.save(consultation);
        return consultationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void cancelConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        if (consultation.getConsultationsStatus() == ConsultationStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed consultation.");
        }

        consultation.setConsultationsStatus(ConsultationStatus.CANCELED);
        consultationRepository.save(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationResponse> getConsultantConsultations(UUID consultantId, Pageable pageable) {
        return consultationRepository.findByConsultantId(consultantId, pageable)
                .map(consultationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationResponse> searchConsultantConsultations(UUID consultantId, String keyword, Pageable pageable) {
        Specification<Consultation> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("consultant").get("id"), consultantId),
                cb.or(
                        cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%")
                )
        );

        return consultationRepository.findAll(spec, pageable)
                .map(consultationMapper::toResponse);
    }

    private void applyConsultantAnswer(
            Consultation consultation,
            ConsultationAnswerRequest request,
            boolean isNewAnswer
    ) {
        consultation.setAnswer(request.getAnswer());
        consultation.setResolutionNotes(request.getResolutionNotes());
        consultation.setConsultantUpdatedAt(Instant.now());

        if (isNewAnswer) {
            consultation.setConsultationsStatus(ConsultationStatus.COMPLETED);
            consultation.setAnsweredAt(Instant.now());
        }
    }

}
