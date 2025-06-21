package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;


public interface NewsService {

    /**
     * Get paginated news
     */
    PageResponse<NewsResponse> getNewsPaginated(int page, int size);

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
     NewsResponse createNews(NewsRequest request, MultipartFile image);

    /**
     * Update an existing news item
     */
    NewsResponse updateNews(Long id, NewsRequest request);

    /**
     * Delete a news item
     */
    void deleteNews(Long id);


}