package com.example.SBA_M.dto.response;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ConsultantProfileResponse {
    private UUID accountId;
    private String bio;
    private Integer maxConcurrentRequests;
    private Integer currentPendingRequests;
    private List<MajorResponse> specialties;
}
