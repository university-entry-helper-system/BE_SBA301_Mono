package com.example.SBA_M.utils;

public enum StatusConsultant {
    ONLINE("Online"),
    OFFLINE("Offline"),
    BUSY("Busy");

    private final String value;

    StatusConsultant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}