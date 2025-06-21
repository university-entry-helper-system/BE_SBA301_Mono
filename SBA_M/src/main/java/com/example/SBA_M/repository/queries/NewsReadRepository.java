package com.example.SBA_M.repository.queries;


import com.example.SBA_M.entity.queries.NewsDocument;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NewsReadRepository extends MongoRepository<NewsDocument, Long> {
    Page<NewsDocument> findByStatus(Status status, Pageable pageable);


}