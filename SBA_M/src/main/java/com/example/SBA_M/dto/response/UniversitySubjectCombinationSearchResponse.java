package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversitySubjectCombinationSearchResponse {
    private Integer universityId;
    private String universityName;
    private Integer universityMajorCountBySubjectCombination;
}
