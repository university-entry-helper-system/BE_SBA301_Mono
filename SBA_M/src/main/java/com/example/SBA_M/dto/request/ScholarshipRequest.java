package com.example.SBA_M.dto.request;

import com.example.SBA_M.entity.commands.Scholarship;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScholarshipRequest {
    private String name;
    private String description;
    private Scholarship.ValueType valueType;
    private Double valueAmount;
    private Scholarship.EligibilityType eligibilityType;
    private Double minScore;
    private String applyLink;
    @NotNull(message = "Hạn nộp không được để trống")
    private Instant applicationDeadline;
    private List<Integer> universityIds;
}
