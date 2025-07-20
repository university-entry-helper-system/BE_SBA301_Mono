package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.NewsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Get paginated news")
    @GetMapping("/paginated")
    public ApiResponse<PageResponse<NewsResponse>> getNewsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<NewsResponse> result = newsService.getNewsPaginated(page, size);
        return ApiResponse.<PageResponse<NewsResponse>>builder()
                .code(1000)
                .message("News fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get news by ID")
    @GetMapping("/{id}")
    public ApiResponse<NewsResponse> getNewsById(@PathVariable Long id) {
        NewsResponse news = newsService.getNewsById(id);
        return ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News fetched successfully")
                .result(news)
                .build();
    }

    @Operation(summary = "Create news with optional image")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createNews(@Valid @ModelAttribute NewsRequest newsRequest) {
        try {
            NewsResponse created = newsService.createNews(newsRequest);
            return ApiResponse.<NewsResponse>builder()
                    .code(1001)
                    .message("News created successfully")
                    .result(created)
                    .build();
        } catch (Exception e) {
            log.error("News creation error: {}", e.getMessage(), e);
            return ApiResponse.builder()
                    .code(400)
                    .message("Invalid input: " + e.getMessage())
                    .build();
        }
    }

    @Operation(summary = "Update a news item")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<NewsResponse> updateNews(
            @PathVariable Long id,
            @Valid @ModelAttribute NewsRequest newsRequest) {
        NewsResponse updated = newsService.updateNews(id, newsRequest);
        return ApiResponse.<NewsResponse>builder()
                .code(1002)
                .message("News updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a news item")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("News deleted successfully")
                .build();
    }

    @Operation(summary = "Search news")
    @GetMapping("/search")
    public ApiResponse<PageResponse<NewsResponse>> searchNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<NewsResponse> result = newsService.searchNews(query, page, size);
        return ApiResponse.<PageResponse<NewsResponse>>builder()
                .code(1000)
                .message("News search successful")
                .result(result)
                .build();
    }

    @Operation(summary = "Get top 5 hot news")
    @GetMapping("/hot")
    public ApiResponse<List<NewsResponse>> getTopHotNews() {
        List<NewsResponse> result = newsService.getTop5HotNews();
        return ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Top 5 hot news fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get news with filter and search (guest & admin)")
    @GetMapping("/api/news")
    public ApiResponse<PageResponse<NewsResponse>> getNewsFiltered(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer minViews,
            @RequestParam(required = false) Integer maxViews,
            @RequestParam(required = false) String newsStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Nếu có filter nâng cao thì dùng advanced, còn lại dùng guest
        boolean isAdvanced = fromDate != null || toDate != null || minViews != null || maxViews != null || (newsStatus != null && !newsStatus.isEmpty());
        PageResponse<NewsResponse> result = isAdvanced
                ? newsService.advancedNewsSearch(search, fromDate, toDate, minViews, maxViews, newsStatus, page, size)
                : newsService.filterNewsByCategoryAndSearch(category, search, page, size);
        return ApiResponse.<PageResponse<NewsResponse>>builder()
                .code(1000)
                .message("News fetched successfully")
                .result(result)
                .build();
    }
}

