package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponse {
    private Long id;
    private UUID accountId;
    private UUID consultantId;
    private String title;
    private String content;
    private String resolutionNotes;
    private String status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
}