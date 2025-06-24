package com.example.SBA_M.dto.response.tuition_search_response;

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
public class AdmissionYearGroup {
    private Integer year;
    private List<AdmissionMethodGroup> methods;
}
