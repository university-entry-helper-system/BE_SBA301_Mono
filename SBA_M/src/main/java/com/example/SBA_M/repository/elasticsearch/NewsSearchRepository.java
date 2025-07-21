package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.queries.NewsSearch;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsSearchRepository extends ElasticsearchRepository<NewsSearch, Long> {
    // Combined search for title or content containing text
    Page<NewsSearch> findByContentContainingOrSummaryContainingAndStatus(String content, String summary, Status status, Pageable pageable);

    // Tìm kiếm full-text và filter nâng cao
    Page<NewsSearch> findByCategoryAndStatus(String category, Status status, Pageable pageable);

    Page<NewsSearch> findByCategoryAndContentContainingOrCategoryAndSummaryContainingAndStatus(
            String category1, String content, String category2, String summary, Status status, Pageable pageable);

    Page<NewsSearch> findByContentContainingOrSummaryContainingAndStatusAndPublishedAtBetweenAndViewCountBetweenAndNewsStatus(
            String content, String summary, Status status, java.time.Instant fromDate, java.time.Instant toDate, Integer minViews, Integer maxViews, String newsStatus, Pageable pageable);

    Page<NewsSearch> findByCategoryAndContentContainingOrCategoryAndSummaryContainingAndStatusAndPublishedAtBetweenAndViewCountBetweenAndNewsStatus(
            String category1, String content, String category2, String summary, Status status, java.time.Instant fromDate, java.time.Instant toDate, Integer minViews, Integer maxViews, String newsStatus, Pageable pageable);

    Page<NewsSearch> findByCategoryAndStatusAndPublishedAtBetweenAndViewCountBetweenAndNewsStatus(
            String category, Status status, java.time.Instant fromDate, java.time.Instant toDate, Integer minViews, Integer maxViews, String newsStatus, Pageable pageable);
}