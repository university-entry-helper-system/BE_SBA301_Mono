package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.page_visit.PageVisitCompareResponse;
import com.example.SBA_M.dto.response.page_visit.VisitCompareResponse;
import com.example.SBA_M.dto.response.page_visit.VisitDailyResponse;

import java.time.LocalDate;
import java.util.List;

public interface  PageVisitService {
    void recordVisit();
    Long getVisitCountBetween(LocalDate start, LocalDate end);
    PageVisitCompareResponse compareVisitChange(LocalDate from1, LocalDate to1, LocalDate from2, LocalDate to2);
    Long getVisitCountOn(LocalDate date);
    VisitCompareResponse compareTodayWithYesterday();
    List<VisitDailyResponse> getDailyVisitCounts(LocalDate from, LocalDate to);
    long getTotalVisitCount();
}
