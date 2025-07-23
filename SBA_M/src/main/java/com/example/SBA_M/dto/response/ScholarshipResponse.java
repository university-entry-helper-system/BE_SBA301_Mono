package com.example.SBA_M.dto.response;

import com.example.SBA_M.entity.commands.Scholarship;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScholarshipResponse {
    private Integer id;
    private String name;
    private String description;
    private Scholarship.ValueType valueType;
    private Double valueAmount;
    private Scholarship.EligibilityType eligibilityType;
    private Double minScore;
    private String applyLink;
    private Instant applicationDeadline;
    private String status; // ACTIVE / EXPIRED

    private List<UniversityShort> universities;

    @Getter
    @Setter
    public static class UniversityShort {
        private Integer id;
        private String shortName;
    }
}
