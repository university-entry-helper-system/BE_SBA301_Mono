package com.example.SBA_M.dto.request;

import com.example.SBA_M.entity.commands.Scholarship;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScholarshipSearchRequest {
    private String name;
    private Scholarship.ValueType valueType;
    private Scholarship.EligibilityType eligibilityType;
    private String status; // "ACTIVE" hoặc "EXPIRED"
}
