package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.ConsultantProfile;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.StatusConsultant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultantProfileRepository extends JpaRepository<ConsultantProfile, Integer>, JpaSpecificationExecutor<ConsultantProfile> {
    Page<ConsultantProfile> findAllByStatus(StatusConsultant status, Pageable pageable);

    @Query("SELECT cp FROM ConsultantProfile cp JOIN cp.account a WHERE a.status = :status")
    Page<ConsultantProfile> findAllByAccountStatus(@Param("status") AccountStatus status, Pageable pageable);
}
