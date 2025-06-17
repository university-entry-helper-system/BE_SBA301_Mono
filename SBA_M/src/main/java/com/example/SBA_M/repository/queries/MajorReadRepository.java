package com.example.SBA_M.repository.queries;

import com.example.SBA_M.entity.queries.MajorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorReadRepository extends MongoRepository<MajorDocument, Long> {
}
