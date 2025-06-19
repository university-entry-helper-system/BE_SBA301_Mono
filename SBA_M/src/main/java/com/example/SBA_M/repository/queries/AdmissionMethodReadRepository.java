package com.example.SBA_M.repository.queries;

import com.example.SBA_M.entity.queries.AdmissionMethodDocument;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdmissionMethodReadRepository extends MongoRepository<AdmissionMethodDocument, Integer> {
    Page<AdmissionMethodDocument> findAllByStatus(Status status, Pageable pageable);
    Optional<AdmissionMethodDocument> findByIdAndStatus(Integer id, Status status);
}
