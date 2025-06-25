package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAdmissionMethodDetailResponse {
    private Integer universityId;
    private String universityName;
    private List<AdmissionMethodDetail> methods;
}
