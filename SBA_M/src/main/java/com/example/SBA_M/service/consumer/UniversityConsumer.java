package com.example.SBA_M.service.consumer;

import com.example.SBA_M.entity.UniversityCategory;
import com.example.SBA_M.entity.UniversityCategoryDocument;
import com.example.SBA_M.entity.UniversityDocument;
import com.example.SBA_M.event.UniversityCreatedEvent;
import com.example.SBA_M.repository.UniversityCategoryRepository;
import com.example.SBA_M.repository.UniversityReadCategoryRepository;
import com.example.SBA_M.repository.UniversityReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniversityConsumer {
    private final UniversityReadRepository universityReadRepository;
    private final UniversityCategoryRepository universityCategoryRepository;
    @KafkaListener(topics = "uni.created", groupId = "sba-group")
    public void consume(UniversityCreatedEvent event) {
        UniversityCategory category = universityCategoryRepository
                .findById(event.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + event.getCategoryId()));
        UniversityCategoryDocument categoryDoc = new UniversityCategoryDocument(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
        UniversityDocument doc = new UniversityDocument(
                event.getId(),
                categoryDoc,
                event.getName(),
                event.getShortName(),
                event.getLogoUrl(),
                event.getFoundingYear(),
                event.getProvince(),
                event.getType(),
                event.getAddress(),
                event.getEmail(),
                event.getPhone(),
                event.getWebsite(),
                event.getDescription(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
        universityReadRepository.save(doc);
    }
}