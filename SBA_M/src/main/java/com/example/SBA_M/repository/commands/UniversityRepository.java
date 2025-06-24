package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    Optional<University> findByIdAndStatus(Integer id, Status status);
}