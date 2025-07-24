package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByYearAndType(Integer year, String type);
    List<Score> findByYear(Integer year);
    List<Score> findByType(String type);
    List<Score> findByYearAndTypeAndSubject(Integer year, String type, String subject);
}