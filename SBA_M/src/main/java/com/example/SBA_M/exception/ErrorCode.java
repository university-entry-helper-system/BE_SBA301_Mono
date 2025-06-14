package com.example.SBA_M.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // General Errors
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Invalid key"),
    INVALID_PARAM(1002, "Invalid parameter: {0}"),

    // Account & User Errors
    USERNAME_EXISTED(1003, "Username already exists"),
    EMAIL_EXISTED(1004, "Email already exists"),
    ACCOUNT_NOT_FOUND(1005, "Account not found"),
    INVALID_CREDENTIALS(1006, "Invalid username or password"),
    ACCOUNT_NOT_ACTIVE(1007, "Account is not active. Please activate your account."),
    ACCOUNT_ALREADY_ACTIVE(1008, "Account is already active."),
    INVALID_ACTIVATION_CODE(1009, "Invalid activation code."),
    ACTIVATION_CODE_EXPIRED(1010, "Activation code has expired. Please request a new one."),
    OLD_PASSWORD_MISMATCH(1011, "Old password does not match."),
    PASSWORD_MISMATCH(1012, "New passwords do not match."),
    ACCOUNT_NOT_DELETED(1013, "Account is not marked as deleted."),

    // Role Errors
    ROLE_NOT_FOUND(1014, "Role not found"),

    // Token Errors
    INVALID_TOKEN(1015, "Invalid token."),
    TOKEN_ALREADY_REVOKED(1016, "Token has already been revoked."),
    PASSWORD_RESET_TOKEN_EXPIRED(1017, "Password reset token has expired."),
    INVALID_PASSWORD_RESET_TOKEN(1018, "Invalid password reset token."),

    // Authorization & Authentication Errors
    UNAUTHENTICATED(1019, "Unauthenticated"),
    ACCESS_DENIED(1020, "You do not have permission to access this resource.");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}