package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.ExamSubject;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Long> {
    boolean existsByName(String name);
    Page<ExamSubject> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);
}
