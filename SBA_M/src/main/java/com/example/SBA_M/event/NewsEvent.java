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
    private boolean flagged;

    public NewsEvent(
            Long id,
            Integer universityId,
            String universityName,
            String title,
            String summary,
            String content,
            String imageUrl,
            String category,
            Integer viewCount,
            String newsStatus, // Change to String
            Instant publishedAt,
            Instant deletedAt,
            Status status,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy,
            boolean flagged
    ) {
        this.setId(id);
        this.setStatus(status);
        this.setCreatedBy(createdBy);
        this.setCreatedAt(createdAt);
        this.setUpdatedBy(updatedBy);
        this.setUpdatedAt(updatedAt);
        this.setDeletedAt(deletedAt);
        this.setFlagged(flagged);
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
        this.deletedAt = deletedAt;
        this.flagged = flagged;
    }
}