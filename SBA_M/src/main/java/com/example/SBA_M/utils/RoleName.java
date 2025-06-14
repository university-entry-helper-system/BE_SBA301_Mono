package com.example.SBA_M.utils;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleName {
    @JsonProperty("user")
    USER,
    @JsonProperty("consultant")
    CONSULTANT,
    @JsonProperty("admin")
    ADMIN
}