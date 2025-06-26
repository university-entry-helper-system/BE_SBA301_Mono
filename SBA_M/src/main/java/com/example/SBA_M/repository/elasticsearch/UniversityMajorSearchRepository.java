package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.commands.UniversityMajor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UniversityMajorSearchRepository extends ElasticsearchRepository<UniversityMajor, Integer> {
    // This interface extends ElasticsearchRepository to provide CRUD operations for UniMajorSearch entities.
    // Additional custom query methods can be defined here if needed.
}
