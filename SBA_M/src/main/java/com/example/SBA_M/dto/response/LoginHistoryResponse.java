package com.example.SBA_M.dto.response;

import lombok.Data;
import java.time.Instant;

@Data
public class LoginHistoryResponse {
    private Instant timestamp;
    private String ip;
    private String device;
} 