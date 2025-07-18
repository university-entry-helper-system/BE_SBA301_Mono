package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordStrengthRequest {
    @NotBlank(message = "Password cannot be blank")
    private String password;
} 