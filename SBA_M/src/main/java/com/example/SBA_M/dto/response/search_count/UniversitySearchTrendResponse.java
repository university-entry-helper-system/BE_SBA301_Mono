package com.example.SBA_M.dto.response.search_count;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UniversitySearchTrendResponse {
    private Integer universityId;
    private String universityName;
    private List<DateCountPointResponse> data;
}
