package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountStatsResponse {
    Long totalAccounts; // Tổng số tài khoản
    Long activeAccounts; // Số tài khoản ACTIVE
    Long inactiveAccounts; // Số tài khoản INACTIVE
    Long bannedAccounts; // Số tài khoản BANNED
    Long deletedAccounts; // Số tài khoản đã xóa (soft delete)
} 