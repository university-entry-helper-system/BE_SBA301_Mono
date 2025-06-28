package com.example.SBA_M.repository.queries;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.utils.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UniversityMajorReadRepository extends MongoRepository<AdmissionEntriesDocument, String> {
    List<AdmissionEntriesDocument> findByUniversityIdAndStatus(Integer universityId, Status status);
    List<AdmissionEntriesDocument> findByUniversityIdAndMajorIdAndStatus(Integer universityId, Long majorId, Status status);
    List<AdmissionEntriesDocument> findByUniversityIdAndSubjectCombinationIdAndStatus(Integer universityId, Long subjectCombinationId, Status status);

    List<AdmissionEntriesDocument> findByUniversityId(Integer universityId);

    List<AdmissionEntriesDocument> findByMethodId(Integer methodId);

    List<AdmissionEntriesDocument> findByMajorId(Long majorId);

    List<AdmissionEntriesDocument> findBySubjectCombinationId(Long subjectCombinationId);
}
