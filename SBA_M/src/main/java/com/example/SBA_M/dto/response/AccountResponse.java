package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    UUID id; // ID tài khoản là UUID
    String username;
    String email;
    String password;
    String fullName;
    String phone;
    String status;
    RoleResponse role; // Thông tin về vai trò của tài khoản
    Instant createdAt;
    Instant updatedAt;
    // Có thể thêm createdBy/updatedBy nếu thực sự cần cho client, nhưng thường là không.
    // String createdBy;
    // String updatedBy;
}