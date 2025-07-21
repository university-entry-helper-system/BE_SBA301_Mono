package com.example.SBA_M.event;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsEvent extends AbstractCreatedEvent<Long> {
    private Integer universityId;
    private String universityName;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private String category;
    private Integer viewCount;
    private String newsStatus; // Change to String
    private Instant publishedAt;
    private Instant deletedAt;
    private Instant releaseDate;

    public NewsEvent(Long id, Integer universityId, String universityName, String title, String summary, String content, String imageUrl, String category, Integer viewCount, String newsStatus, Instant publishedAt, Instant releaseDate, Instant deletedAt, Status status, Instant createdAt, String createdBy, Instant updatedAt, String updatedBy) {
        this.setId(id);
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setCreatedBy(createdBy);
        this.setUpdatedAt(updatedAt);
        this.setUpdatedBy(updatedBy);
        this.universityId = universityId;
        this.universityName = universityName;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.viewCount = viewCount;
        this.newsStatus = newsStatus;
        this.publishedAt = publishedAt;
        this.releaseDate = releaseDate;
        this.deletedAt = deletedAt;
    }
}