package com.example.SBA_M.service.impl;


import com.example.SBA_M.dto.response.PageVisitCompareResponse;
import com.example.SBA_M.dto.response.VisitCompareResponse;
import com.example.SBA_M.dto.response.VisitDailyResponse;
import com.example.SBA_M.entity.commands.PageVisit;
import com.example.SBA_M.repository.commands.PageVisitRepository;
import com.example.SBA_M.service.PageVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageVisitServiceImpl implements PageVisitService {
    private final PageVisitRepository pageVisitRepository;


    @Override
    public void recordVisit() {
        LocalDate today = LocalDate.now();
        PageVisit visit = pageVisitRepository.findByCreatedAt(today);

        if (visit == null) {
            visit = new PageVisit();
            visit.setCreatedAt(today);
            visit.setCount(1L);
        } else {
            visit.setCount(visit.getCount() + 1);
        }

        pageVisitRepository.save(visit);
    }

    private Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    @Override
    public Long getVisitCountOn(LocalDate date) {
        PageVisit visit = pageVisitRepository.findByCreatedAt(date);
        return visit != null ? visit.getCount() : 0L;
    }

    @Override
    public VisitCompareResponse compareTodayWithYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        long todayCount = getVisitCountOn(today);
        long yesterdayCount = getVisitCountOn(yesterday);

        double change;
        if (yesterdayCount == 0) {
            change = todayCount > 0 ? 100.0 : 0.0;
        } else {
            change = ((double) (todayCount - yesterdayCount) / yesterdayCount) * 100;
        }

        return new VisitCompareResponse(todayCount, yesterdayCount, change);
    }


    @Override
    public Long getVisitCountBetween(LocalDate start, LocalDate end) {
        List<PageVisit> visits = pageVisitRepository.findAllByCreatedAtBetween(start, end);
        return visits.stream().mapToLong(PageVisit::getCount).sum();
    }

    @Override
    public PageVisitCompareResponse compareVisitChange(LocalDate from1, LocalDate to1, LocalDate from2, LocalDate to2) {
        long count1 = getVisitCountBetween(from1, to1);
        long count2 = getVisitCountBetween(from2, to2);

        double change;
        if (count1 == 0) {
            change = count2 > 0 ? 100.0 : 0.0;
        } else {
            change = ((double) (count2 - count1) / count1) * 100;
        }

        return new PageVisitCompareResponse(count1, count2, change);
    }

    @Override
    public List<VisitDailyResponse> getDailyVisitCounts(LocalDate from, LocalDate to) {
        return pageVisitRepository.findAllByCreatedAtBetween(from, to).stream()
                .map(v -> new VisitDailyResponse(v.getCreatedAt(), v.getCount()))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
    }

    @Override
    public long getTotalVisitCount() {
        return pageVisitRepository.findAll().stream()
                .mapToLong(PageVisit::getCount)
                .sum();
    }

}
