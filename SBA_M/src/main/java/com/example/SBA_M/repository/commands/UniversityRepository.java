package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    Optional<University> findByIdAndStatus(Integer id, Status status);

    Page<University> findByStatus(Status status, Pageable pageable);
    
    List<University> findAllByStatus(Status status);
    
    Optional<University> findByUniversityCodeAndStatus(String universityCode, Status status);
    
    Optional<University> findByNameAndStatus(String name, Status status);
    
    Optional<University> findByShortNameAndStatus(String shortName, Status status);
}