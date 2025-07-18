package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeRequest {
    
    @NotBlank(message = "Action is required")
    @Pattern(regexp = "^(PROMOTE|DEMOTE)$", message = "Action must be either 'PROMOTE' or 'DEMOTE'")
    private String action; // PROMOTE: nâng role, DEMOTE: hạ role
    
    private String reason; // Lý do thay đổi role (optional)
} 