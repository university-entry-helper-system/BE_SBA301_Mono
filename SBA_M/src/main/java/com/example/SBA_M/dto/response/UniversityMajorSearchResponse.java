package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityMajorSearchResponse {
    private String universityName;
    private Integer universityMajorCountByMajor;
    private List<String> methods;
}
