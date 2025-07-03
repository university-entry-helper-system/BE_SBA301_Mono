package com.example.SBA_M.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityMethodRequest {
    private Integer universityId;
    private Integer admissionMethodId;
    private Integer year;
    private String notes;
    private String conditions;
    private String regulations;
    private String admissionTime;
}

