package com.example.SBA_M.repository;

import com.example.SBA_M.entity.UniversityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityCategoryRepository extends JpaRepository<UniversityCategory, Long> {
}