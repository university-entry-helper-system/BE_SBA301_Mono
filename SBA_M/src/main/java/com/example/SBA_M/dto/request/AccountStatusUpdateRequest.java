package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountStatusUpdateRequest {
    @NotNull(message = "Status cannot be null")
    private AccountStatus status;
}