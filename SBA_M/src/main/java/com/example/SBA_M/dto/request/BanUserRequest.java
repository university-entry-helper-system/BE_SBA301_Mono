package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BanUserRequest {
    @NotNull(message = "Banned status cannot be null")
    private boolean banned;
} 