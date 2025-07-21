package com.example.SBA_M.dto.response.page_visit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VisitDailyResponse {
    private LocalDate date;
    private long count;
}
