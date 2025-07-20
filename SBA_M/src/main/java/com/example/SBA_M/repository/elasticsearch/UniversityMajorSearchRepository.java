package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.utils.Status;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityMajorSearchRepository extends ElasticsearchRepository<UniversityMajorSearch, String> {
    List<UniversityMajorSearch> findByUniversityId(Integer universityId);

    List<UniversityMajorSearch> findBySubjectCombinationId(Long subjectCombinationId);

    List<UniversityMajorSearch> findBySubjectCombinationIdAndUniversityNameContainingIgnoreCaseAndStatusAndYear(Long subjectCombinationId, String universityName, Status status, Integer year);

    List<UniversityMajorSearch> findBySubjectCombinationIdAndStatusAndYear(Long subjectCombinationId, Status status, Integer year);

    List<UniversityMajorSearch> findByMajorIdAndUniversityNameContainingIgnoreCaseAndStatusAndYear(Long majorId, String universityName, Status status, Integer year);

    List<UniversityMajorSearch> findByMajorIdAndStatusAndYear(Long majorId, Status status, Integer year);
}
