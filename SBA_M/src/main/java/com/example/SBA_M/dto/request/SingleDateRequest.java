package com.example.SBA_M.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SingleDateRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
