package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.search_count.UniversitySearchStatResponse;
import com.example.SBA_M.dto.response.search_count.UniversitySearchTrendResponse;
import com.example.SBA_M.entity.commands.SearchCount;

import java.time.LocalDate;
import java.util.List;

public interface SearchCountService {

    void recordSearch(Integer universityId);

    List<UniversitySearchStatResponse> getUniversitySearchStats(LocalDate from, LocalDate to);
    UniversitySearchStatResponse getTopUniversityOnDate(LocalDate date);
    List<UniversitySearchTrendResponse> getUniversitySearchTrends(LocalDate from, LocalDate to);
    UniversitySearchStatResponse getTopUniversityInRange(LocalDate from, LocalDate to);
    UniversitySearchTrendResponse getTrendOfUniversity(Integer universityId, LocalDate from, LocalDate to);
    public UniversitySearchStatResponse getTopUniversityToDay();


    SearchCount getById(Long id);

    List<SearchCount> getAll();

    void deleteById(Long id);

    List<SearchCount> getByDateRange(LocalDate from, LocalDate to);

    SearchCount getByDateAndUniversity(LocalDate date, Integer universityId);
}
