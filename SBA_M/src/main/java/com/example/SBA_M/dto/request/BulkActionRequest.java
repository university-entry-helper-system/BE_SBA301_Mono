package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class BulkActionRequest {
    @NotNull(message = "Action cannot be null")
    private String action; // ACTIVATE, DEACTIVATE, BAN, EXPORT, SEND_NOTIFICATION

    @NotEmpty(message = "IDs cannot be empty")
    private List<String> ids; // UUID dạng string

    private NotificationRequest notification;

    @Data
    public static class NotificationRequest {
        private String title;
        private String content;
    }
} 