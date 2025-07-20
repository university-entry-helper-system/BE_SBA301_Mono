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
public class MajorAdmissionYearGroup {
    private Integer year;
    private String methodName;
    private List<MajorMethodGroup> methods;
}
