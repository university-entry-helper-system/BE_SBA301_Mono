package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityAdmissionMethodResponse {
    private Integer id;
    private Integer universityId;
    private String universityName;
    private Integer admissionMethodId;
    private String admissionMethodName;
    private Integer year;
    private String notes;
    private String conditions;
    private String regulations;
    private String admissionTime;
}
