package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.CampusType;
import com.example.SBA_M.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CampusTypeRepository extends JpaRepository<CampusType, Integer> {
    Optional<CampusType> findByIdAndStatus(Integer id, Status status);
    
    List<CampusType> findAllByStatus(Status status);

    // Phân trang
    Page<CampusType> findAllByStatus(Status status, Pageable pageable);

    // Search + phân trang
    Page<CampusType> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
    
    Optional<CampusType> findByNameAndStatus(String name, Status status);
} 