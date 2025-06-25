package com.example.SBA_M.repository.queries;

import com.example.SBA_M.entity.queries.UniversityEntriesDocument;
import com.example.SBA_M.utils.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityAdmissionMethodReadRepository  extends MongoRepository<UniversityEntriesDocument, Integer> {
    List<UniversityEntriesDocument> findByUniversityIdAndYearAndStatus(Integer universityId, Integer year, Status status);
    List<UniversityEntriesDocument> findByMethodIdAndYearAndStatus(Integer methodId, Integer year, Status status);
}
