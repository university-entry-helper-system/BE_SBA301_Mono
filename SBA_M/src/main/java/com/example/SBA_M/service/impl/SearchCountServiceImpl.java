package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.response.search_count.DateCountPointResponse;
import com.example.SBA_M.dto.response.search_count.UniversitySearchStatResponse;
import com.example.SBA_M.dto.response.search_count.UniversitySearchTrendResponse;
import com.example.SBA_M.entity.commands.SearchCount;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.repository.commands.SearchCountRepository;
import com.example.SBA_M.service.SearchCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<UniversitySearchStatResponse> getUniversitySearchStats(LocalDate from, LocalDate to) {
        return searchCountRepository.findAllByCreatedAtBetween(from, to).stream()
                .collect(Collectors.groupingBy(sc -> sc.getUniversity().getId(),
                        Collectors.summarizingLong(SearchCount::getSearchCount)))
                .entrySet().stream()
                .map(entry -> {
                    Integer universityId = entry.getKey();
                    long total = entry.getValue().getSum();
                    String name = searchCountRepository.findFirstByUniversity_Id(universityId)
                            .getUniversity().getName();
                    return new UniversitySearchStatResponse(universityId, name, total);
                })
                .sorted((a, b) -> Long.compare(b.getTotalSearches(), a.getTotalSearches()))
                .toList();
    }

    @Override
    public UniversitySearchStatResponse getTopUniversityOnDate(LocalDate date) {
        List<SearchCount> records = searchCountRepository.findAllByCreatedAtBetween(date, date);

        if (records.isEmpty()) return null;

        SearchCount top = records.stream()
                .max(Comparator.comparingLong(SearchCount::getSearchCount))
                .orElseThrow(); // không thể null ở đây vì vừa check empty ở trên

        return new UniversitySearchStatResponse(
                top.getUniversity().getId(),
                top.getUniversity().getName(),
                top.getSearchCount()
        );
    }
    @Override
    public UniversitySearchStatResponse getTopUniversityToDay() {
        LocalDate date = LocalDate.now();
        List<SearchCount> records = searchCountRepository.findAllByCreatedAtBetween(date, date);

        if (records.isEmpty()) return null;

        SearchCount top = records.stream()
                .max(Comparator.comparingLong(SearchCount::getSearchCount))
                .orElseThrow(); // không thể null ở đây vì vừa check empty ở trên

        return new UniversitySearchStatResponse(
                top.getUniversity().getId(),
                top.getUniversity().getName(),
                top.getSearchCount()
        );
    }


    @Override
    public List<UniversitySearchTrendResponse> getUniversitySearchTrends(LocalDate from, LocalDate to) {
        List<SearchCount> all = searchCountRepository.findAllByCreatedAtBetween(from, to);

        return all.stream()
                .collect(Collectors.groupingBy(
                        sc -> sc.getUniversity().getId(),
                        Collectors.groupingBy(
                                SearchCount::getCreatedAt,
                                Collectors.summingLong(SearchCount::getSearchCount)
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    Integer universityId = entry.getKey();
                    Map<LocalDate, Long> dateCountMap = entry.getValue();
                    String name = all.stream()
                            .filter(sc -> sc.getUniversity().getId().equals(universityId))
                            .findFirst()
                            .map(sc -> sc.getUniversity().getName())
                            .orElse("Unknown");

                    List<DateCountPointResponse> data = dateCountMap.entrySet().stream()
                            .map(e -> new DateCountPointResponse(e.getKey(), e.getValue()))
                            .sorted(Comparator.comparing(DateCountPointResponse::getDate))
                            .toList();

                    return new UniversitySearchTrendResponse(universityId, name, data);
                })
                .toList();
    }

    @Override
    public UniversitySearchStatResponse getTopUniversityInRange(LocalDate from, LocalDate to) {
        List<SearchCount> all = searchCountRepository.findAllByCreatedAtBetween(from, to);

        return all.stream()
                .collect(Collectors.groupingBy(sc -> sc.getUniversity().getId(),
                        Collectors.summingLong(SearchCount::getSearchCount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    Integer universityId = entry.getKey();
                    long total = entry.getValue();
                    String name = all.stream()
                            .filter(sc -> sc.getUniversity().getId().equals(universityId))
                            .findFirst()
                            .map(sc -> sc.getUniversity().getName())
                            .orElse("Unknown");

                    return new UniversitySearchStatResponse(universityId, name, total);
                })
                .orElse(null);
    }

    @Override
    public UniversitySearchTrendResponse getTrendOfUniversity(Integer universityId, LocalDate from, LocalDate to) {
        List<SearchCount> records = searchCountRepository.findAllByUniversity_IdAndCreatedAtBetween(universityId, from, to);

        String universityName = records.stream()
                .findFirst()
                .map(sc -> sc.getUniversity().getName())
                .orElse("Unknown");

        List<DateCountPointResponse> data = records.stream()
                .collect(Collectors.groupingBy(SearchCount::getCreatedAt, Collectors.summingLong(SearchCount::getSearchCount)))
                .entrySet().stream()
                .map(e -> new DateCountPointResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DateCountPointResponse::getDate))
                .toList();

        return new UniversitySearchTrendResponse(universityId, universityName, data);
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
        return searchCountRepository.findAllByCreatedAtBetween(from, to);
    }

    @Override
    public SearchCount getByDateAndUniversity(LocalDate date, Integer universityId) {
        return searchCountRepository.findByCreatedAtAndUniversity_Id(date, universityId);
    }
}
