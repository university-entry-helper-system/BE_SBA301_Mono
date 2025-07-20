package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.commands.SearchCount;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.repository.commands.SearchCountRepository;
import com.example.SBA_M.service.SearchCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchCountServiceImpl implements SearchCountService {

    private final SearchCountRepository searchCountRepository;

    @Override
    public void recordSearch(Integer universityId) {
        LocalDate today = LocalDate.now();
        SearchCount existing = searchCountRepository.findByCreatedAtAndUniversity_Id(today, universityId);

        if (existing != null) {
            existing.setSearchCount(existing.getSearchCount() + 1);
            searchCountRepository.save(existing);
        } else {
            SearchCount newEntry = new SearchCount();
            newEntry.setCreatedAt(today);
            newEntry.setSearchCount(1L);

            University university = new University();
            university.setId(universityId);
            newEntry.setUniversity(university);

            searchCountRepository.save(newEntry);
        }
    }

    @Override
    public SearchCount getById(Long id) {
        return searchCountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SearchCount not found with id: " + id));
    }

    @Override
    public List<SearchCount> getAll() {
        return searchCountRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        searchCountRepository.deleteById(id);
    }

    @Override
    public List<SearchCount> getByDateRange(LocalDate from, LocalDate to) {
        return searchCountRepository.findAllByCreatedAtDateBetween(from, to);
    }

    @Override
    public SearchCount getByDateAndUniversity(LocalDate date, Integer universityId) {
        return searchCountRepository.findByCreatedAtAndUniversity_Id(date, universityId);
    }
}
