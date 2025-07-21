package com.example.SBA_M.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DateRangeRequest {

    private LocalDate from;
    private LocalDate to;
}
