package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.NewsCategory;
import com.example.SBA_M.utils.NewsStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class News extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private NewsCategory category = NewsCategory.OTHER;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "news_status")
    private NewsStatus newsStatus = NewsStatus.PUBLISHED;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "release_date")
    private Instant releaseDate;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    protected void onCreate() {
        if (publishedAt == null && newsStatus == NewsStatus.PUBLISHED) {
            publishedAt = Instant.now();
        }
    }
}
