package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionMethodRepository extends JpaRepository<AdmissionMethod, Integer> {
}
