package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionMethodRepository extends JpaRepository<AdmissionMethod, Integer> {
    List<AdmissionMethod> findAllByIdInAndStatus(List<Integer> ids, Status status);
    Page<AdmissionMethod> findAllByStatus(Status status, Pageable pageable);
    Page<AdmissionMethod> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);

    Optional<AdmissionMethod> findByIdAndStatus(Integer id, Status status);
}
