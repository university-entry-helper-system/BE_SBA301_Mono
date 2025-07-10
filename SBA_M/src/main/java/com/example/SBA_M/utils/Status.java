package com.example.SBA_M.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("deleted")
    DELETED,
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("pending")
    PENDING
}
