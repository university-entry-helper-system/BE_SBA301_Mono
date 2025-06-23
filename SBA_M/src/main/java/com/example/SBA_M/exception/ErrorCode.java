package com.example.SBA_M.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // General Errors
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(1002, "Invalid parameter: {0}", HttpStatus.BAD_REQUEST),

    // Account & User Errors
    USERNAME_EXISTED(1003, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1004, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1021, "Phone number already exists", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND(1005, "Account not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1006, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVE(1007, "Account is not active. Please activate your account.", HttpStatus.FORBIDDEN),
    ACCOUNT_ALREADY_ACTIVE(1008, "Account is already active.", HttpStatus.BAD_REQUEST),
    INVALID_ACTIVATION_CODE(1009, "Invalid activation code.", HttpStatus.BAD_REQUEST),
    ACTIVATION_CODE_EXPIRED(1010, "Activation code has expired. Please request a new one.", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_MISMATCH(1011, "Old password does not match.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1012, "New passwords do not match.", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_DELETED(1013, "Account is not marked as deleted.", HttpStatus.BAD_REQUEST),
    MAJOR_NOT_FOUND(1014, "Major not found", HttpStatus.NOT_FOUND),
    // Role Errors
    ROLE_NOT_FOUND(1014, "Role not found", HttpStatus.NOT_FOUND),

    // Token Errors
    INVALID_TOKEN(1015, "Invalid token.", HttpStatus.UNAUTHORIZED),
    TOKEN_ALREADY_REVOKED(1016, "Token has already been revoked.", HttpStatus.BAD_REQUEST),
    PASSWORD_RESET_TOKEN_EXPIRED(1017, "Password reset token has expired.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_RESET_TOKEN(1018, "Invalid password reset token.", HttpStatus.BAD_REQUEST),

    // Authorization & Authentication Errors
    UNAUTHENTICATED(1019, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1020, "You do not have permission to access this resource.", HttpStatus.FORBIDDEN),
    UNIVERSITY_NOT_FOUND(1021, "University not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(404, "University category not found", HttpStatus.NOT_FOUND),
    NEWS_NOT_FOUND(1022, "News not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNIVERSITY_MAJOR_NOT_FOUND(1023, "University major not found", HttpStatus.NOT_FOUND),
    EXAM_SUBJECT_NOT_FOUND(1024, "Exam subject not found", HttpStatus.NOT_FOUND),
    ;
    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}