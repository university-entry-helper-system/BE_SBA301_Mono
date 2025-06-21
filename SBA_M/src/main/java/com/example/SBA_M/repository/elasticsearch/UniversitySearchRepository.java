package com.example.SBA_M.repository.elasticsearch;

import com.example.SBA_M.entity.queries.UniSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UniversitySearchRepository extends ElasticsearchRepository<UniSearch, Integer> {

}