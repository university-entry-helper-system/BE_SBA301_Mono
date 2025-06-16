package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MajorCreatedEvent {
    private Long id;
    private String name;
    private String code;
    private String majorGroup;
    private String degree;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}