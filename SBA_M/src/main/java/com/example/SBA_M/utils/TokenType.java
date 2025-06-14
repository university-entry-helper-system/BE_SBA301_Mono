package com.example.SBA_M.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TokenType {
    @JsonProperty("access")
    ACCESS,
    @JsonProperty("refresh")
    REFRESH,
    @JsonProperty("email_verify")
    EMAIL_VERIFY,
    @JsonProperty("password_reset")
    PASSWORD_RESET,
    @JsonProperty("other")
    OTHER
}
