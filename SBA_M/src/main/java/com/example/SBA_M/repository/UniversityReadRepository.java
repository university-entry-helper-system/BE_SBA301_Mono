package com.example.SBA_M.repository;

import com.example.SBA_M.entity.UniversityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityReadRepository extends MongoRepository<UniversityDocument, Long> {
}
