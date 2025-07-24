package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequest {
    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotBlank(message = "Score URL is required")
    private String scoreUrl;

    @NotBlank(message = "Type is required")
    private String type;
}