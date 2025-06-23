package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.entity.commands.UniversityMajor;
import com.example.SBA_M.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByIdAndStatus(Long id, Status status);

}