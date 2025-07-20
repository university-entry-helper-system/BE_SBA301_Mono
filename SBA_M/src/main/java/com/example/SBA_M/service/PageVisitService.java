package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.PageVisitCompareResponse;
import com.example.SBA_M.dto.response.VisitCompareResponse;
import com.example.SBA_M.dto.response.VisitDailyResponse;

import java.time.LocalDate;
import java.util.Date;
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
