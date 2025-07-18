package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "Password must be at least {min} characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;'\"|,.<>/?]).{8,}$",
            message = "Password must contain at least 1 uppercase, 1 lowercase, 1 number and 1 special character")
    private String newPassword;
} 