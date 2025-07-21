package com.example.SBA_M.dto.response.page_visit;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitCompareResponse {
    private long todayCount;
    private long yesterdayCount;
    private double percentageChange;
}
