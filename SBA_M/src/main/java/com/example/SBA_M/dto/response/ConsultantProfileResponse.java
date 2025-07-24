package com.example.SBA_M.dto.response;


import com.example.SBA_M.utils.ConsultationStatus;
import com.example.SBA_M.utils.StatusConsultant;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ConsultantProfileResponse {
    private UUID accountId;
    private String fullName;
    private String bio;
    private StatusConsultant status;
    private List<MajorResponse> specialties;
}
