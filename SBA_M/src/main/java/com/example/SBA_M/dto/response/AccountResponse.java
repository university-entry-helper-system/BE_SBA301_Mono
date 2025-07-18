package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    UUID id;
    String username;
    String email;
    String fullName;
    String phone;
    String status;
    String gender;
    LocalDate dob;
    RoleResponse role;
    Integer loginCount;
    Instant lastLoginAt;
    String createdBy;
    String updatedBy;
    Instant createdAt;
    Instant updatedAt;
}