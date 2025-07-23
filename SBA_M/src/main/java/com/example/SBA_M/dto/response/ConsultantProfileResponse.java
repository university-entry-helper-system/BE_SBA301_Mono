package com.example.SBA_M.dto.response;


import com.example.SBA_M.utils.Gender;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ConsultantProfileResponse {
    private UUID accountId;
    private String fullName;
    private String bio;
    private Gender gender;
    private List<MajorResponse> specialties;
}
