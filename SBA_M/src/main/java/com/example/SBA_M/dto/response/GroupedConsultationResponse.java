package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedConsultationResponse {

    private UUID senderId;

    private List<ConsultationResponse> consultations;
}