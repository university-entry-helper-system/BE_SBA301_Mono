package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityAdmissionMethodSummaryResponse {
    private String universityName;
    private String note;
}
