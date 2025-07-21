package com.example.SBA_M.utils;

public enum NewsStatus {
    PUBLISHED("Đã xuất bản"),
    DRAFT("Bản nháp"),
    ARCHIVED("Đã lưu trữ");

    private final String vietnameseLabel;

    NewsStatus(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    public String getVietnameseLabel() {
        return vietnameseLabel;
    }
}

