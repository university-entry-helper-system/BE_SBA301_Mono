package com.example.SBA_M.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PasswordStrengthResponse {
    private int score; // 0-4
    private String level; // WEAK, MEDIUM, STRONG
    private List<String> suggestions;
} 