package com.example.SBA_M.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PageVisitCompareRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to2;
}
