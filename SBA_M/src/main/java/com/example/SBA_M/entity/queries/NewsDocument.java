package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "news")
@EqualsAndHashCode(callSuper = true)
public class NewsDocument extends AbstractDocument<Long> {

    @Field("university")
    private UniversityDocument university;

    @Field("title")
    private String title;

    @Field("summary")
    private String summary;

    @Field("content")
    private String content;

    @Field("image_url")
    private String imageUrl;

    @Field("category")
    private String category;

    @Field("view_count")
    private Integer viewCount = 0;

    @Field("news_status")
    private String newsStatus = "PUBLISHED";

    @Field("published_at")
    private Instant publishedAt;

    public NewsDocument(Long id, UniversityDocument university, String title,
                        String summary, String content, String imageUrl, String category,
                        Integer viewCount, String newsStatus, Instant publishedAt,
                        Status status, Instant createdAt, String createdBy,
                        Instant updatedAt, String updatedBy) {
        this.setId(id);
        this.university = university;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.viewCount = viewCount;
        this.newsStatus = newsStatus;
        this.publishedAt = publishedAt;
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setCreatedBy(createdBy);
        this.setUpdatedAt(updatedAt);
        this.setUpdatedBy(updatedBy);
    }
}