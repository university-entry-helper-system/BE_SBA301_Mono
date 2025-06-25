package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionMethodDetail {
    private Integer methodId;
    private String methodName;
    private Integer year;
    private String notes;
    private String conditions;
    private String regulations;
    private String admissionTime;
}
