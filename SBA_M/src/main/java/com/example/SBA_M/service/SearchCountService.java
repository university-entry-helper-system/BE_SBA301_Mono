package com.example.SBA_M.service;

import com.example.SBA_M.entity.commands.SearchCount;

import java.time.LocalDate;
import java.util.List;

public interface SearchCountService {

    void recordSearch(Integer universityId);

    SearchCount getById(Long id);

    List<SearchCount> getAll();

    void deleteById(Long id);

    List<SearchCount> getByDateRange(LocalDate from, LocalDate to);

    SearchCount getByDateAndUniversity(LocalDate date, Integer universityId);
}
