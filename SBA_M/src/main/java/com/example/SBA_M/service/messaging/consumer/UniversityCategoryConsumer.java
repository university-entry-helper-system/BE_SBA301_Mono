package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.event.UniversityCategoryEvent;
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

    @KafkaListener(topics = "university.category.created", groupId = "sba-group")
    public void consumeCreated(UniversityCategoryEvent event) {
        log.info("Consuming university category created event: {}", event);

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
        log.info("University category document created: {}", document.getId());
    }

    @KafkaListener(topics = "university.category.updated", groupId = "sba-group")
    public void consumeUpdated(UniversityCategoryEvent event) {
        log.info("Consuming university category updated event: {}", event);

        UniversityCategoryDocument existingDocument = universityCategoryReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("University category document not found with id: " + event.getId()));

        existingDocument.setName(event.getName());
        existingDocument.setDescription(event.getDescription());
        existingDocument.setStatus(event.getStatus());
        existingDocument.setUpdatedAt(event.getUpdatedAt());
        existingDocument.setUpdatedBy(event.getUpdatedBy());

        universityCategoryReadRepository.save(existingDocument);
        log.info("University category document updated: {}", existingDocument.getId());
    }

    @KafkaListener(topics = "university.category.deleted", groupId = "sba-group")
    public void consumeDeleted(UniversityCategoryEvent event) {
        log.info("Consuming university category deleted event: {}", event);

        UniversityCategoryDocument existingDocument = universityCategoryReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("University category document not found with id: " + event.getId()));

        // Implementing soft delete by updating status
        existingDocument.setStatus(event.getStatus());
        existingDocument.setUpdatedAt(event.getUpdatedAt());
        existingDocument.setUpdatedBy(event.getUpdatedBy());

        universityCategoryReadRepository.save(existingDocument);
        log.info("University category document soft deleted: {}", existingDocument.getId());
    }
}