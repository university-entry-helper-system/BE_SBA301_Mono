package com.example.SBA_M.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockResponse {
    private Long id;
    private String name;
    private String description;
    private List<SubjectCombinationResponse> subjectCombinations;
    private com.example.SBA_M.utils.Status status;
} 