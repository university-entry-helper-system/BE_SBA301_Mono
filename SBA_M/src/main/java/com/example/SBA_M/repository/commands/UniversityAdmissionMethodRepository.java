package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UniversityAdmissionMethodRepository extends JpaRepository<UniversityAdmissionMethod, Integer> {
    Page<UniversityAdmissionMethod> findByStatus(Status status, Pageable pageable);
    Optional<UniversityAdmissionMethod> findByIdAndStatus(Integer id, Status status);

    Page<UniversityAdmissionMethod> findByStatusAndUniversityId(Status status, Integer universityId, Pageable pageable);
}
