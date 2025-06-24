package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.queries.UniCategorySearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UniversityCategorySearchRepository extends ElasticsearchRepository<UniCategorySearch, Integer> {
}
