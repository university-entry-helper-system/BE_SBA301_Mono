package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ConsultationCreateRequest {
    @NotNull(message = "Sender ID is required.")
    private UUID sender;

    @NotNull(message = "Receiver ID is required.")
    private UUID receiver;

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must not exceed 255 characters.")
    private String title;

    @NotBlank(message = "Content is required.")
    private String content;
}