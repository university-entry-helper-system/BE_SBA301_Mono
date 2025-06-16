package com.example.SBA_M.repository;

import com.example.SBA_M.entity.MajorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorReadRepository extends MongoRepository<MajorDocument, Long> {
}
