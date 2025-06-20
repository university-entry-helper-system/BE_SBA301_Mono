package com.example.SBA_M.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UniversityAdmissionMethodResponse {
    private String admissionMethodName;
    private Integer year;
    private String conditions;
    private String regulations;
    private String admissionTime;
}
