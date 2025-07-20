package com.example.SBA_M.dto.response.major_search_response;

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
public class MajorAdmissionResponse {
    private String universityId;
    private String universityName;
    private String majorId;
    private List<MajorAdmissionYearGroup> years;
}
