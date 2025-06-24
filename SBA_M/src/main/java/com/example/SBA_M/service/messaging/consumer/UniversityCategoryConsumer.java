package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.UniCategorySearch;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.event.UniversityCategoryEvent;
import com.example.SBA_M.repository.elasticsearch.UniversityCategorySearchRepository;
import com.example.SBA_M.repository.queries.UniversityReadCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniversityCategoryConsumer {
    private final UniversityReadCategoryRepository universityCategoryReadRepository;
    private final UniversityCategorySearchRepository universityCategorySearchRepository;

    @KafkaListener(topics = "university.category.created", groupId = "sba-group")
    public void consumeCreated(UniversityCategoryEvent event) {
        try {
            log.info("Consuming university category created event: {}", event);

            // Save to MongoDB
            UniversityCategoryDocument document = new UniversityCategoryDocument(
                    event.getId(),
                    event.getName(),
                    event.getDescription(),
                    event.getStatus(),
                    event.getCreatedAt(),
                    event.getCreatedBy(),
                    event.getUpdatedAt(),
                    event.getUpdatedBy()
            );

            universityCategoryReadRepository.save(document);
            log.info("University category document created in MongoDB: {}", document.getId());

            // Save to Elasticsearch
            UniCategorySearch uniSearch = UniCategorySearch.builder()
                    .name(event.getName())
                    .description(event.getDescription())
                    .build();

            uniSearch.setId(event.getId());
            uniSearch.setStatus(event.getStatus());
            uniSearch.setCreatedAt(event.getCreatedAt());
            uniSearch.setCreatedBy(event.getCreatedBy());
            uniSearch.setUpdatedAt(event.getUpdatedAt());
            uniSearch.setUpdatedBy(event.getUpdatedBy());

            universityCategorySearchRepository.save(uniSearch);
            log.info("University category document created in Elasticsearch: {}", uniSearch.getId());
        } catch (Exception e) {
            log.error("Error processing university category created event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "university.category.updated", groupId = "sba-group")
    public void consumeUpdated(UniversityCategoryEvent event) {
        try {
            log.info("Consuming university category updated event: {}", event);

            // Update in MongoDB
            UniversityCategoryDocument existingDocument = universityCategoryReadRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University category document not found with id: " + event.getId()));

            existingDocument.setName(event.getName());
            existingDocument.setDescription(event.getDescription());
            existingDocument.setStatus(event.getStatus());
            existingDocument.setUpdatedAt(event.getUpdatedAt());
            existingDocument.setUpdatedBy(event.getUpdatedBy());

            universityCategoryReadRepository.save(existingDocument);
            log.info("University category document updated in MongoDB: {}", existingDocument.getId());

            // Update in Elasticsearch
            UniCategorySearch existingSearch = universityCategorySearchRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University category search document not found with id: " + event.getId()));

            existingSearch.setName(event.getName());
            existingSearch.setDescription(event.getDescription());
            existingSearch.setStatus(event.getStatus());
            existingSearch.setUpdatedAt(event.getUpdatedAt());
            existingSearch.setUpdatedBy(event.getUpdatedBy());

            universityCategorySearchRepository.save(existingSearch);
            log.info("University category document updated in Elasticsearch: {}", existingSearch.getId());
        } catch (Exception e) {
            log.error("Error processing university category updated event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "university.category.deleted", groupId = "sba-group")
    public void consumeDeleted(UniversityCategoryEvent event) {
        try {
            log.info("Consuming university category deleted event: {}", event);

            // Update in MongoDB
            UniversityCategoryDocument existingDocument = universityCategoryReadRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University category document not found with id: " + event.getId()));

            // Implementing soft delete by updating status
            existingDocument.setStatus(event.getStatus());
            existingDocument.setUpdatedAt(event.getUpdatedAt());
            existingDocument.setUpdatedBy(event.getUpdatedBy());

            universityCategoryReadRepository.save(existingDocument);
            log.info("University category document soft deleted in MongoDB: {}", existingDocument.getId());

            // Update in Elasticsearch
            UniCategorySearch existingSearch = universityCategorySearchRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("University category search document not found with id: " + event.getId()));

            existingSearch.setStatus(event.getStatus());
            existingSearch.setUpdatedAt(event.getUpdatedAt());
            existingSearch.setUpdatedBy(event.getUpdatedBy());

            universityCategorySearchRepository.save(existingSearch);
            log.info("University category document soft deleted in Elasticsearch: {}", existingSearch.getId());
        } catch (Exception e) {
            log.error("Error processing university category deleted event: {}", e.getMessage(), e);
        }
    }
}