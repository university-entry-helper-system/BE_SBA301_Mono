package com.example.SBA_M.dto.response.search_count;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateCountPointResponse {
    private LocalDate date;
    private long count;
}
