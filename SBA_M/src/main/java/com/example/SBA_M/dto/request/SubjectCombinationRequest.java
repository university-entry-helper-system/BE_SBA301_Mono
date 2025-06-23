package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCombinationRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 10, message = "Name must not exceed 10 characters")
    private String name;

    private String description;

    @NotEmpty(message = "At least one exam subject is required")
    private List<Long> examSubjectIds;
}