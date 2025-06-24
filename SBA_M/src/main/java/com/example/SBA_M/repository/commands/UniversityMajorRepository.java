package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.UniversityMajor;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityMajorRepository extends JpaRepository<UniversityMajor, Integer> {
    Optional<UniversityMajor> findByIdAndStatus(Integer id, Status status);

    Page<UniversityMajor> findByStatus(Status status, Pageable pageable);
}

