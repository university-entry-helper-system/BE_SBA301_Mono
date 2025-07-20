package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageVisitCompareResponse {
    private long count1;
    private long count2;
    private double percentageChange;
}
