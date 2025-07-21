package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.NewsCategory;
import com.example.SBA_M.utils.NewsStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    @NotNull(message = "University ID is required")
    private Integer universityId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Summary is required")
    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

    @NotBlank(message = "Content is required")
    private String content;

    private NewsCategory category = NewsCategory.OTHER; // Default category

    private NewsStatus newsStatus = NewsStatus.PUBLISHED;

    private Instant publishedAt;

    private Instant releaseDate;

    private MultipartFile image;
}