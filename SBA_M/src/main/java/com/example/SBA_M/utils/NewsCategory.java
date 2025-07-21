package com.example.SBA_M.utils;

public enum NewsCategory {
    ADMISSION_INFO("Thông tin tuyển sinh"),
    EXAM_SCHEDULE("Lịch thi"),
    SCHOLARSHIP("Học bổng"),
    GUIDANCE("Hướng dẫn thủ tục"),
    REGULATION_CHANGE("Thay đổi quy định"),
    EVENT("Sự kiện"),
    RESULT_ANNOUNCEMENT("Công bố kết quả"),
    SYSTEM_NOTIFICATION("Thông báo hệ thống"),
    OTHER("Khác");

    private final String vietnameseLabel;

    NewsCategory(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    public String getVietnameseLabel() {
        return vietnameseLabel;
    }
}
