package com.example.SBA_M.entity.commands;

import com.example.SBA_M.entity.AbstractEntity;
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

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(length = 100)
    private String category;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(length = 20)
    private String status = "Published";

    @Column(name = "published_at")
    private Instant publishedAt;
}
