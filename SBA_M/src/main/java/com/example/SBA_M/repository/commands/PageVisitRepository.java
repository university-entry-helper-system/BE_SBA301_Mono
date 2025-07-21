package com.example.SBA_M.repository.commands;


import com.example.SBA_M.entity.commands.PageVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PageVisitRepository extends JpaRepository<PageVisit, Long> {
    PageVisit findByCreatedAt(LocalDate createdAt);
    List<PageVisit> findAllByCreatedAtBetween(LocalDate start, LocalDate end);
}
