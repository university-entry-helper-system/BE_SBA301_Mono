package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityCreatedEvent {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String shortName;
    private String logoUrl;
    private Integer foundingYear;
    private String province;
    private String type;
    private String address;
    private String email;
    private String phone;
    private String website;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}