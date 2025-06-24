package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.ExamSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Long> {
    boolean existsByName(String name);

}
