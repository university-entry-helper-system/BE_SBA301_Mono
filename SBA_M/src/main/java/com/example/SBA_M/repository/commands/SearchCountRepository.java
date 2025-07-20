package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.SearchCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchCountRepository extends JpaRepository<SearchCount, Long> {
    List<SearchCount> findAllByCreatedAtDateBetween(LocalDate from, LocalDate to);

    SearchCount findByCreatedAtAndUniversity_Id(LocalDate date, Integer universityId);
}
