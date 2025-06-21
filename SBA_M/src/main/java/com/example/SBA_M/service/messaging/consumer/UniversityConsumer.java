package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.event.UniversityEvent;
import com.example.SBA_M.repository.commands.UniversityCategoryRepository;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniversityConsumer {
    private final UniversityReadRepository universityReadRepository;
    private final UniversityCategoryRepository universityCategoryRepository;
    @KafkaListener(topics = "uni.created", groupId = "sba-group")
    public void consume(UniversityEvent event) {
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
        UniversityDocument doc = new UniversityDocument(
                event.getId(),
                categoryDoc,
                event.getName(),
                event.getShortName(),
                event.getLogoUrl(),
                event.getFoundingYear(),
                event.getProvince(),
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
    }

    @KafkaListener(topics = "uni.updated", groupId = "sba-group")
    public void consumeUpdated(UniversityEvent event) {

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

        // Update fields
        existingDoc.setCategory(categoryDoc);
        existingDoc.setName(event.getName());
        existingDoc.setShortName(event.getShortName());
        existingDoc.setLogoUrl(event.getLogoUrl());
        existingDoc.setFoundingYear(event.getFoundingYear());
        existingDoc.setProvince(event.getProvince());
        existingDoc.setAddress(event.getAddress());
        existingDoc.setEmail(event.getEmail());
        existingDoc.setPhone(event.getPhone());
        existingDoc.setWebsite(event.getWebsite());
        existingDoc.setDescription(event.getDescription());
        existingDoc.setStatus(event.getStatus());
        existingDoc.setUpdatedAt(event.getUpdatedAt());
        existingDoc.setUpdatedBy(event.getUpdatedBy());

        universityReadRepository.save(existingDoc);
    }

    @KafkaListener(topics = "uni.deleted", groupId = "sba-group")
    public void consumeDeleted(UniversityEvent event) {

        UniversityDocument existingDoc = universityReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("University document not found with ID: " + event.getId()));

        // Update status to deleted
        existingDoc.setStatus(event.getStatus());
        existingDoc.setUpdatedAt(event.getUpdatedAt());
        existingDoc.setUpdatedBy(event.getUpdatedBy());

        universityReadRepository.save(existingDoc);
    }
}