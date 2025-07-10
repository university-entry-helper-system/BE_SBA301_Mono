package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ConsultantProfileRequest {

    @NotNull(message = "Account ID is required.")
    private UUID accountId;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters.")
    private String bio;

    @NotNull(message = "Max concurrent requests is required.")
    private Integer maxConcurrentRequests;

    private List<Long> specialtyIds;
}
