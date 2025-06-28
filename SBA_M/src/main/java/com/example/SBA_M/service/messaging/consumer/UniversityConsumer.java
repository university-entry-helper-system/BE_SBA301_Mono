package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.entity.queries.UniCategorySearch;
import com.example.SBA_M.entity.queries.UniSearch;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.event.UniversityEvent;
import com.example.SBA_M.repository.commands.UniversityCategoryRepository;
import com.example.SBA_M.repository.elasticsearch.UniversityCategorySearchRepository;
import com.example.SBA_M.repository.elasticsearch.UniversitySearchRepository;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniversityConsumer {
    private final UniversityReadRepository universityReadRepository;
    private final UniversityCategoryRepository universityCategoryRepository;
    private final UniversitySearchRepository universitySearchRepository;

    @KafkaListener(topics = "uni.created", groupId = "sba-group")
    public void consume(UniversityEvent event) {
        try {
            log.info("Consuming university created event: {}", event);

            UniversityCategory category = universityCategoryRepository
                    .findById(event.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + event.getCategoryId()));

            Province province = new Province();
            // Save to MongoDB
            UniversityCategoryDocument categoryDoc = new UniversityCategoryDocument(
                    category.getId(),
                    category.getName(),
                    category.getDescription(),
                    category.getStatus(),
                    category.getCreatedAt(),
                    category.getCreatedBy(),
                    category.getUpdatedAt(),
                    category.getUpdatedBy()
            );

            UniversityDocument doc = new UniversityDocument(
                    event.getId(),
                    categoryDoc,
                    event.getName(),
                    event.getShortName(),
                    event.getLogoUrl(),
                    event.getFoundingYear(),
                    province,
                    event.getAddress(),
                    event.getEmail(),
                    event.getPhone(),
                    event.getWebsite(),
                    event.getDescription(),
                    event.getStatus(),
                    event.getCreatedAt(),
                    event.getCreatedBy(),
                    event.getUpdatedAt(),
                    event.getUpdatedBy()
            );

            universityReadRepository.save(doc);
            log.info("University document created in MongoDB: {}", doc.getId());

            // Save to Elasticsearch
            UniCategorySearch uniCategorySearch = UniCategorySearch.builder()
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
            Province provinceSearch = new Province();

            // Set the fields from AbstractElasticsearchDocument
            uniCategorySearch.setId(category.getId());
            uniCategorySearch.setStatus(category.getStatus());
            uniCategorySearch.setCreatedAt(category.getCreatedAt());
            uniCategorySearch.setCreatedBy(category.getCreatedBy());
            uniCategorySearch.setUpdatedAt(category.getUpdatedAt());
            uniCategorySearch.setUpdatedBy(category.getUpdatedBy());

            UniSearch uniSearch = UniSearch.builder()
                    .category(uniCategorySearch)
                    .name(event.getName())
                    .shortName(event.getShortName())
                    .logoUrl(event.getLogoUrl())
                    .foundingYear(event.getFoundingYear())
                    .province(provinceSearch)
                    .address(event.getAddress())
                    .email(event.getEmail())
                    .phone(event.getPhone())
                    .website(event.getWebsite())
                    .description(event.getDescription())
                    .build();

            uniSearch.setId(event.getId());
            uniSearch.setStatus(event.getStatus());
            uniSearch.setCreatedAt(event.getCreatedAt());
            uniSearch.setCreatedBy(event.getCreatedBy());
            uniSearch.setUpdatedAt(event.getUpdatedAt());
            uniSearch.setUpdatedBy(event.getUpdatedBy());

            universitySearchRepository.save(uniSearch);
            log.info("University document created in Elasticsearch: {}", uniSearch.getId());
        } catch (Exception e) {
            log.error("Error processing university created event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "uni.updated", groupId = "sba-group")
    public void consumeUpdated(UniversityEvent event) {
        try {
            log.info("Consuming university updated event: {}", event);

            // Update in MongoDB
            UniversityDocument existingDoc = universityReadRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University document not found with ID: " + event.getId()));

            UniversityCategory category = universityCategoryRepository
                    .findById(event.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + event.getCategoryId()));

            UniversityCategoryDocument categoryDoc = new UniversityCategoryDocument(
                    category.getId(),
                    category.getName(),
                    category.getDescription(),
                    category.getStatus(),
                    category.getCreatedAt(),
                    category.getCreatedBy(),
                    category.getUpdatedAt(),
                    category.getUpdatedBy()
            );
            Province provinceDoc = new Province();

            // Update fields
            existingDoc.setCategory(categoryDoc);
            existingDoc.setName(event.getName());
            existingDoc.setShortName(event.getShortName());
            existingDoc.setLogoUrl(event.getLogoUrl());
            existingDoc.setFoundingYear(event.getFoundingYear());
            existingDoc.setProvince(provinceDoc);
            existingDoc.setAddress(event.getAddress());
            existingDoc.setEmail(event.getEmail());
            existingDoc.setPhone(event.getPhone());
            existingDoc.setWebsite(event.getWebsite());
            existingDoc.setDescription(event.getDescription());
            existingDoc.setStatus(event.getStatus());
            existingDoc.setUpdatedAt(event.getUpdatedAt());
            existingDoc.setUpdatedBy(event.getUpdatedBy());

            universityReadRepository.save(existingDoc);
            log.info("University document updated in MongoDB: {}", existingDoc.getId());

            // Update in Elasticsearch
            UniSearch existingSearch = universitySearchRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University search document not found with ID: " + event.getId()));

            // Create or update category search
            UniCategorySearch uniCategorySearch = UniCategorySearch.builder()
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
            uniCategorySearch.setId(category.getId());
            uniCategorySearch.setStatus(category.getStatus());
            uniCategorySearch.setCreatedAt(category.getCreatedAt());
            uniCategorySearch.setCreatedBy(category.getCreatedBy());
            uniCategorySearch.setUpdatedAt(category.getUpdatedAt());
            uniCategorySearch.setUpdatedBy(category.getUpdatedBy());

            Province provinceSearch = new Province();

            // Update search fields
            existingSearch.setCategory(uniCategorySearch);
            existingSearch.setName(event.getName());
            existingSearch.setShortName(event.getShortName());
            existingSearch.setLogoUrl(event.getLogoUrl());
            existingSearch.setFoundingYear(event.getFoundingYear());
            existingSearch.setProvince(provinceSearch);
            existingSearch.setAddress(event.getAddress());
            existingSearch.setEmail(event.getEmail());
            existingSearch.setPhone(event.getPhone());
            existingSearch.setWebsite(event.getWebsite());
            existingSearch.setDescription(event.getDescription());
            existingSearch.setStatus(event.getStatus());
            existingSearch.setUpdatedAt(event.getUpdatedAt());
            existingSearch.setUpdatedBy(event.getUpdatedBy());

            universitySearchRepository.save(existingSearch);
            log.info("University document updated in Elasticsearch: {}", existingSearch.getId());
        } catch (Exception e) {
            log.error("Error processing university updated event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "uni.deleted", groupId = "sba-group")
    public void consumeDeleted(UniversityEvent event) {
        try {
            log.info("Consuming university deleted event: {}", event);

            // Update in MongoDB
            UniversityDocument existingDoc = universityReadRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University document not found with ID: " + event.getId()));

            // Update status to deleted
            existingDoc.setStatus(event.getStatus());
            existingDoc.setUpdatedAt(event.getUpdatedAt());
            existingDoc.setUpdatedBy(event.getUpdatedBy());

            universityReadRepository.save(existingDoc);
            log.info("University document soft deleted in MongoDB: {}", existingDoc.getId());

            // Update in Elasticsearch
            try {
                UniSearch existingSearch = universitySearchRepository.findById(event.getId())
                        .orElseThrow(() -> new RuntimeException("University search document not found with ID: " + event.getId()));

                existingSearch.setStatus(event.getStatus());
                existingSearch.setUpdatedAt(event.getUpdatedAt());
                existingSearch.setUpdatedBy(event.getUpdatedBy());

                universitySearchRepository.save(existingSearch);
                log.info("University document soft deleted in Elasticsearch: {}", existingSearch.getId());
            } catch (Exception e) {
                log.error("Error updating Elasticsearch for deleted university: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Error processing university deleted event: {}", e.getMessage(), e);
        }
    }


}