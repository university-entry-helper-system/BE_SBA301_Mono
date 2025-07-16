package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Campus;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer> {
    Optional<Campus> findByIdAndStatus(Integer id, Status status);
    
    Page<Campus> findByStatus(Status status, Pageable pageable);
    
    List<Campus> findAllByStatus(Status status);
    
    List<Campus> findByUniversityIdAndStatus(Integer universityId, Status status);
    
    Page<Campus> findByProvinceIdAndStatus(Integer provinceId, Status status, Pageable pageable);
    
    List<Campus> findByUniversityIdAndProvinceIdAndStatus(Integer universityId, Integer provinceId, Status status);
    
    Optional<Campus> findByUniversityIdAndCampusCodeAndStatus(Integer universityId, String campusCode, Status status);
    
    List<Campus> findByUniversityIdAndIsMainCampusAndStatus(Integer universityId, Boolean isMainCampus, Status status);
} 