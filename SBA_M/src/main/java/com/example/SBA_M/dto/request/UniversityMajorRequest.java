package com.example.SBA_M.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityMajorRequest {
    private Integer universityId; // Required for create

    private Long majorId;         // Required for create

    private Integer year;

    private Integer quota;

    private String notes;

    private List<Integer> admissionMethodIds;

    private List<Long> subjectCombinationIds;

    private Double scores;
}
