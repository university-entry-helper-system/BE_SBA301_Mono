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
}