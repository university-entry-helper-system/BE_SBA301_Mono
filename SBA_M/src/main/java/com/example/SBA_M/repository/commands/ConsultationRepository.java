package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>, JpaSpecificationExecutor<Consultation> {
    Page<Consultation> findByAccountId(UUID accountId, Pageable pageable);

    Page<Consultation> findByConsultantId(UUID consultantId, Pageable pageable);

    Page<Consultation> findByAccountIdAndConsultantId(UUID accountId, UUID consultantId, Pageable pageable);
}
