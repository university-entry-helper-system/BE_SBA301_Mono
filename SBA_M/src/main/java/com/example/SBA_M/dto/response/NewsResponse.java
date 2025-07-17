package com.example.SBA_M.dto.response;

import com.example.SBA_M.utils.NewsStatus;
import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private UniversityResponse university;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private String category;
    private Integer viewCount;
    private NewsStatus newsStatus;
    private Instant publishedAt;
    private Instant deletedAt;
    private Status status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
}