package com.example.SBA_M.dto.response.search_count;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UniversitySearchStatResponse {
    private Integer universityId;
    private String universityName;
    private long totalSearches;
}
