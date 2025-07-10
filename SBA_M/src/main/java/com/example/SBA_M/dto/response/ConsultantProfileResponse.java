package com.example.SBA_M.dto.response;

import com.example.SBA_M.utils.Status;

import java.util.List;
import java.util.UUID;

public class ConsultantProfileResponse {
    private UUID accountId;
    private String bio;
    private Integer maxConcurrentRequests;
    private Integer currentPendingRequests;
    private List<MajorResponse> specialties;
}
