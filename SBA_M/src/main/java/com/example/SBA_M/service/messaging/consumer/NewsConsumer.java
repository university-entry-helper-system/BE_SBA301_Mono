package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.NewsDocument;
import com.example.SBA_M.entity.queries.NewsSearch;
import com.example.SBA_M.entity.queries.UniSearch;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.event.NewsEvent;
import com.example.SBA_M.repository.elasticsearch.NewsSearchRepository;
import com.example.SBA_M.repository.elasticsearch.UniversitySearchRepository;
import com.example.SBA_M.repository.queries.NewsReadRepository;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsConsumer {
    private final NewsReadRepository newsReadRepository;
    private final UniversityReadRepository universityReadRepository;
    private final NewsSearchRepository newsSearchRepository;
    private final UniversitySearchRepository universitySearchRepository;

    @KafkaListener(topics = "news.created", groupId = "sba-group")
    public void consume(NewsEvent event) {
        UniversityDocument university = universityReadRepository.findById(event.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + event.getUniversityId()));
        UniSearch uniSearch = universitySearchRepository.findById(event.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University search document not found with ID: " + event.getUniversityId()));

        // Save to MongoDB
        NewsDocument doc = new NewsDocument(
                event.getId(),
                university,
                event.getTitle(),
                event.getSummary(),
                event.getContent(),
                event.getImageUrl(),
                event.getCategory(),
                event.getViewCount(),
                event.getNewsStatus(),
                event.getPublishedAt(),
                event.getDeletedAt(),
                event.getStatus(),
                event.getCreatedAt(),
                event.getCreatedBy(),
                event.getUpdatedAt(),
                event.getUpdatedBy()
        );
        newsReadRepository.save(doc);

        // Save to Elasticsearch
        NewsSearch newsSearch = NewsSearch.builder()
                .university(uniSearch)
                .summary(event.getSummary())
                .content(event.getContent())
                .imageUrl(event.getImageUrl())
                .category(event.getCategory())
                .viewCount(event.getViewCount())
                .newsStatus(event.getNewsStatus()) // Now String, no conversion needed
                .publishedAt(event.getPublishedAt())
                .build();

        // Set abstract document fields
        newsSearch.setId(event.getId());
        newsSearch.setStatus(event.getStatus());
        newsSearch.setCreatedAt(event.getCreatedAt());
        newsSearch.setCreatedBy(event.getCreatedBy());
        newsSearch.setUpdatedAt(event.getUpdatedAt());
        newsSearch.setUpdatedBy(event.getUpdatedBy());

        newsSearchRepository.save(newsSearch);
    }

    @KafkaListener(topics = "news.updated", groupId = "sba-group")
    public void consumeUpdated(NewsEvent event) {
        // Update MongoDB document
        NewsDocument existingDoc = newsReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("News document not found with ID: " + event.getId()));

        UniversityDocument university = universityReadRepository.findById(event.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + event.getUniversityId()));

        // Update MongoDB fields
        existingDoc.setUniversity(university);
        existingDoc.setTitle(event.getTitle());
        existingDoc.setSummary(event.getSummary());
        existingDoc.setContent(event.getContent());
        existingDoc.setImageUrl(event.getImageUrl());
        existingDoc.setCategory(event.getCategory());
        existingDoc.setViewCount(event.getViewCount());
        existingDoc.setNewsStatus(event.getNewsStatus());
        existingDoc.setPublishedAt(event.getPublishedAt());
        existingDoc.setStatus(event.getStatus());
        existingDoc.setUpdatedAt(event.getUpdatedAt());
        existingDoc.setUpdatedBy(event.getUpdatedBy());

        newsReadRepository.save(existingDoc);

        // Update Elasticsearch document
        NewsSearch existingSearch = newsSearchRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("News search document not found with ID: " + event.getId()));

        UniSearch uniSearch = universitySearchRepository.findById(event.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University search document not found with ID: " + event.getUniversityId()));

        // Update fields with correct names
        existingSearch.setSummary(event.getSummary());
        existingSearch.setContent(event.getContent());
        existingSearch.setImageUrl(event.getImageUrl());
        existingSearch.setCategory(event.getCategory());
        existingSearch.setViewCount(event.getViewCount());
        existingSearch.setNewsStatus(event.getNewsStatus()); // Now String, no conversion needed
        existingSearch.setPublishedAt(event.getPublishedAt());
        existingSearch.setUniversity(uniSearch);

        // Update abstract document fields
        existingSearch.setStatus(event.getStatus());
        existingSearch.setUpdatedAt(event.getUpdatedAt());
        existingSearch.setUpdatedBy(event.getUpdatedBy());

        newsSearchRepository.save(existingSearch);
    }

    @KafkaListener(topics = "news.deleted", groupId = "sba-group")
    public void consumeDeleted(NewsEvent event) {
        // Update MongoDB document status
        NewsDocument existingDoc = newsReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("News document not found with ID: " + event.getId()));

        existingDoc.setStatus(event.getStatus());
        existingDoc.setUpdatedAt(event.getUpdatedAt());
        existingDoc.setUpdatedBy(event.getUpdatedBy());
        existingDoc.setDeletedAt(event.getDeletedAt()); // Set deletedAt for soft delete
        newsReadRepository.save(existingDoc);

        // Update Elasticsearch document status
        NewsSearch existingSearch = newsSearchRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("News search document not found with ID: " + event.getId()));

        existingSearch.setStatus(event.getStatus());
        existingSearch.setUpdatedAt(event.getUpdatedAt());
        existingSearch.setUpdatedBy(event.getUpdatedBy());
        newsSearchRepository.save(existingSearch);
    }
}