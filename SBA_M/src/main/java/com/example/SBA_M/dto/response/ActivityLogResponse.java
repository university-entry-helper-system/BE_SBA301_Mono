package com.example.SBA_M.dto.response;

import lombok.Data;
import java.time.Instant;

@Data
public class ActivityLogResponse {
    private String action;
    private Instant timestamp;
    private String ip;
} 