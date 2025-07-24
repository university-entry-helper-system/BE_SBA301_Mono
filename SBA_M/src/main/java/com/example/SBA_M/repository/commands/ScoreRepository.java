package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
}