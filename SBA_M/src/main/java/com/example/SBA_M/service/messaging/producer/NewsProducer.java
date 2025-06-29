package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.News;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.event.NewsEvent;
import com.example.SBA_M.repository.commands.NewsRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.minio.MinioService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsProducer {
    private final NewsRepository newsRepository;
    private final UniversityRepository universityRepository;
    private final KafkaTemplate<String, NewsEvent> kafkaTemplate;
    private final MinioService minioService;
    public NewsResponse createNews(NewsRequest request, MultipartFile image, String username) {
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with id: " + request.getUniversityId()));
        String presignedImageUrl = null;
        if (image != null && !image.isEmpty()) {
            presignedImageUrl = minioService.uploadFileAndGetPresignedUrl(image);
        }
        News news = new News();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setSummary(request.getSummary());
        news.setImageUrl(presignedImageUrl);
        news.setCategory(request.getCategory());
        news.setUniversity(university);
        news.setViewCount(0);
        news.setNewStatus(request.getNewsStatus());
        news.setStatus(Status.ACTIVE);
        news.setCreatedBy(username);
        news.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        news.setUpdatedBy(username);
        news.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        News savedNews = newsRepository.save(news);

        var publishedAt = "PUBLISHED".equals(savedNews.getNewStatus()) ?
                LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant() :
                null;

        NewsEvent event = new NewsEvent(
                savedNews.getId(),
                savedNews.getUniversity().getId(),
                savedNews.getUniversity().getName(),
                savedNews.getTitle(),
                savedNews.getSummary(),
                savedNews.getContent(),
                savedNews.getImageUrl(),
                savedNews.getCategory(),
                savedNews.getViewCount(),
                savedNews.getNewStatus(),
                publishedAt,
                savedNews.getStatus(),
                savedNews.getCreatedAt(),
                savedNews.getCreatedBy(),
                savedNews.getUpdatedAt(),
                savedNews.getUpdatedBy()
        );

        log.info("Sending create news message: {}", event);
        kafkaTemplate.send("news.created", event);

        return mapToResponse(savedNews);
    }

    public NewsResponse updateNews(Long id, NewsRequest request, String username) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        University university = universityRepository.findById(request.getUniversityId()).orElseThrow(
                () -> new RuntimeException("University not found with id: " + request.getUniversityId()));
        existingNews.setTitle(request.getTitle());
        existingNews.setContent(request.getContent());
        existingNews.setSummary(request.getSummary());
        existingNews.setImageUrl(request.getImageUrl());
        existingNews.setCategory(request.getCategory());
        existingNews.setUniversity(university);
        existingNews.setNewStatus(request.getNewsStatus());
        existingNews.setUpdatedBy(username);
        existingNews.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        News updatedNews = newsRepository.save(existingNews);

        var publishedAt = "PUBLISHED".equals(updatedNews.getNewStatus()) ?
                LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant() :
                null;

        NewsEvent event = new NewsEvent(
            updatedNews.getId(),
            updatedNews.getUniversity().getId(),
            updatedNews.getUniversity().getName(),
            updatedNews.getTitle(),
            updatedNews.getSummary(),
            updatedNews.getContent(),
            updatedNews.getImageUrl(),
            updatedNews.getCategory(),
            updatedNews.getViewCount(),
            updatedNews.getNewStatus(),
            publishedAt,
            updatedNews.getStatus(),
            updatedNews.getCreatedAt(),
            updatedNews.getCreatedBy(),
            updatedNews.getUpdatedAt(),
            updatedNews.getUpdatedBy()
        );

        log.info("Sending update news message: {}", event);
        kafkaTemplate.send("news.updated", event);

        return mapToResponse(updatedNews);
    }

    public void deleteNews(Long id, String username) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        // Soft delete by changing status
        news.setStatus(Status.DELETED);
        news.setUpdatedBy(username);
        news.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        News deletedNews = newsRepository.save(news);

        var publishedAt = "PUBLISHED".equals(deletedNews.getNewStatus()) ?
                LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant() :
                null;

        NewsEvent event = new NewsEvent(
            deletedNews.getId(),
            deletedNews.getUniversity().getId(),
            deletedNews.getUniversity().getName(),
            deletedNews.getTitle(),
            deletedNews.getSummary(),
            deletedNews.getContent(),
            deletedNews.getImageUrl(),
            deletedNews.getCategory(),
            deletedNews.getViewCount(),
            deletedNews.getNewStatus(),
            publishedAt,
            deletedNews.getStatus(),
            deletedNews.getCreatedAt(),
            deletedNews.getCreatedBy(),
            deletedNews.getUpdatedAt(),
            deletedNews.getUpdatedBy()
        );

        log.info("Sending delete news message for ID: {}", id);
        kafkaTemplate.send("news.deleted", event);
    }

    // Helper method to map entity to response DTO
    private NewsResponse mapToResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .summary(news.getSummary())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .category(news.getCategory())
                .university(mapToUniversityResponse(news.getUniversity()))
                .viewCount(news.getViewCount())
                .newsStatus(news.getNewStatus())
                .status(news.getStatus())
                .createdAt(news.getCreatedAt())
                .createdBy(news.getCreatedBy())
                .updatedAt(news.getUpdatedAt())
                .updatedBy(news.getUpdatedBy())
                .build();
    }

    // Helper method to map University entity to UniversityResponse
    private UniversityResponse mapToUniversityResponse(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .name(university.getName())
                .shortName(university.getShortName())
                .logoUrl(university.getLogoUrl())
                .foundingYear(university.getFoundingYear())
                .province(university.getProvince())
                .address(university.getAddress())
                .email(university.getEmail())
                .phone(university.getPhone())
                .website(university.getWebsite())
                .description(university.getDescription())
                .categoryId(university.getCategory().getId())
                .status(university.getStatus())
                .createdAt(university.getCreatedAt())
                .createdBy(university.getCreatedBy())
                .updatedAt(university.getUpdatedAt())
                .updatedBy(university.getUpdatedBy())
                .build();
    }
}