package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.ConsultantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultantProfileRepository extends JpaRepository<ConsultantProfile, UUID>, JpaSpecificationExecutor<ConsultantProfile> {
}
