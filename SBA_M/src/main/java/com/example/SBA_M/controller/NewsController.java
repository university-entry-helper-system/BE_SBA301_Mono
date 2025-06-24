package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/news")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class NewsController {
    private final NewsService newsService;
    private final ObjectMapper objectMapper;

    public NewsController(NewsService newsService, ObjectMapper objectMapper) {
        this.newsService = newsService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Get paginated news", description = "Retrieve news items with pagination")
    @GetMapping("/paginated")
    public ResponseEntity<PageResponse<NewsResponse>> getNewsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(newsService.getNewsPaginated(page, size));
    }

    @Operation(summary = "Get news by ID", description = "Retrieve a news item by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable Long id) {
        NewsResponse news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @Operation(summary = "Create news with optional image", description = "Create a new news item with optional image upload")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNews(
            @RequestParam("news") String newsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            log.info("Received news request: {}", newsJson);
            NewsRequest newsRequest = objectMapper.readValue(newsJson, NewsRequest.class);
            NewsResponse created = newsService.createNews(newsRequest, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error processing news creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid JSON format or image: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a news item", description = "Update an existing news item with the provided request data")
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequest newsRequest) {
        NewsResponse updated = newsService.updateNews(id, newsRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a news item", description = "Delete a news item by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("News deleted successfully");
    }

    @Operation(summary = "Search news", description = "Search news by content or summary")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<NewsResponse>> searchNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(newsService.searchNews(query, page, size));
    }
}