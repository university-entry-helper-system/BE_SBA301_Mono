package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {

    /**
     * Get paginated news
     */
    PageResponse<NewsResponse> getNewsPaginated(int page, int size);

    PageResponse<NewsResponse> getNewsPaginatedByStatus(int page, int size);

    /**
     * Get news by ID
     */
    NewsResponse getNewsById(Long id);


    /**
     * Search news by keyword
     */
    PageResponse<NewsResponse> searchNews(String keyword, int page, int size);


    /**
     * Create a new news item
     */
     NewsResponse createNews(NewsRequest request);

    /**
     * Update an existing news item
     */
    NewsResponse updateNews(Long id, NewsRequest request);

    /**
     * Delete a news item
     */
    void deleteNews(Long id);

    /**
     * Get top N hot news
     */

    /**
     * Get top 5 hot news (category là bất kỳ giá trị nào thuộc enum NewsCategory)
     */
    List<NewsResponse> getTop5HotNews();

    /**
     * Guest: Filter news by category and full-text search
     */
    PageResponse<NewsResponse> filterNewsByCategoryAndSearch(String category, String search, int page, int size);

    /**
     * Admin/User: Advanced search and filtering
     */
    PageResponse<NewsResponse> advancedNewsSearch(String search, String fromDate, String toDate, Integer minViews, Integer maxViews, String newsStatus, String category, int page, int size);

}