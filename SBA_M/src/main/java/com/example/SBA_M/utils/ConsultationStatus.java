package com.example.SBA_M.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ConsultationStatus {
    @JsonProperty("pending")
    PENDING,

    @JsonProperty("completed")
    COMPLETED,

    @JsonProperty("cancelled")
    CANCELED;
}
