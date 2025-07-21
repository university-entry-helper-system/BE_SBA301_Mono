package com.example.SBA_M.dto.response;

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
public class UniversityMajorResponse {
    private Integer id;

    private Integer universityId;
    private String universityName;

    private Long majorId;
    private String majorName;

    private String uniMajorName;

    private Double score;

    private Integer quota;

    private String notes;

    private Integer year;

    private List<AdmissionMethodResponse> admissionMethods;

    private List<SubjectCombinationResponse> subjectCombinations;
}
