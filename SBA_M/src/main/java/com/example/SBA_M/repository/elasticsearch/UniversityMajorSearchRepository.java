package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityMajorSearchRepository extends ElasticsearchRepository<UniversityMajorSearch, String> {
    List<UniversityMajorSearch> findByUniversityId(Integer universityId);

}
