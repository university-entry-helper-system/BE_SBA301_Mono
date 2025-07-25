package com.example.SBA_M.repository.queries;

import com.example.SBA_M.entity.queries.NewsDocument;
import com.example.SBA_M.utils.NewsStatus;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NewsReadRepository extends MongoRepository<NewsDocument, Long> {

    Page<NewsDocument> findByStatus(Status status, Pageable pageable);
    Page<NewsDocument> findByStatusAndNewsStatus(Status status, NewsStatus newsStatus, Pageable pageable);

    /**
     * Lấy top N hot news theo tiêu chí:
     * - newsStatus = 'PUBLISHED'
     * - status = ACTIVE
     * - imageUrl != null
     * - category là bất kỳ giá trị nào thuộc enum NewsCategory
     * Sắp xếp: viewCount giảm dần, publishedAt giảm dần
     */
    @Query(value = "{ 'newsStatus': ?0, 'status': ?1, 'imageUrl': { $ne: null }, 'category': { $in: ?2 } }", sort = "{ 'viewCount': -1, 'publishedAt': -1 }")
    List<NewsDocument> findTopNHotNews(String newsStatus, Status status, List<String> categories, Pageable pageable);
}
