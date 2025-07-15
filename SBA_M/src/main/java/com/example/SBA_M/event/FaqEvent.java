package com.example.SBA_M.event;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqEvent {
    private Long id;
    private String question;
    private String answer;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String status; // e.g., ACTIVE, DELETED
}