package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.dto.request.UniversityCategoryRequest;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.event.UniversityCategoryEvent;
import com.example.SBA_M.repository.commands.UniversityCategoryRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityCategoryProducer {
    private final UniversityCategoryRepository universityCategoryRepository;
    private final KafkaTemplate<String, UniversityCategoryEvent> kafkaTemplate;

    public UniversityCategoryResponse createUniversityCategory(UniversityCategoryRequest request, String username) {
        UniversityCategory category = new UniversityCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(Status.ACTIVE);
        category.setCreatedBy(username);
        category.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        category.setUpdatedBy(username);
        category.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        UniversityCategory savedCategory = universityCategoryRepository.save(category);

        UniversityCategoryEvent event = new UniversityCategoryEvent(
            savedCategory.getId(),
            savedCategory.getName(),
            savedCategory.getDescription(),
            savedCategory.getStatus(),
            savedCategory.getCreatedAt(),
            savedCategory.getCreatedBy(),
            savedCategory.getUpdatedAt(),
            savedCategory.getUpdatedBy()
        );

        log.info("Sending create category message: {}", event);
        kafkaTemplate.send("university.category.created", event);

        // Convert to response DTO before returning
        return mapToResponse(savedCategory);
    }

    public UniversityCategoryResponse updateUniversityCategory(Integer id, UniversityCategoryRequest request, String username) {
        UniversityCategory existingCategory = universityCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University category not found with id: " + id));

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());
        existingCategory.setUpdatedBy(username);
        existingCategory.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        UniversityCategory updatedCategory = universityCategoryRepository.save(existingCategory);

        UniversityCategoryEvent event = new UniversityCategoryEvent(
            updatedCategory.getId(),
            updatedCategory.getName(),
            updatedCategory.getDescription(),
            updatedCategory.getStatus(),
            updatedCategory.getCreatedAt(),
            updatedCategory.getCreatedBy(),
            updatedCategory.getUpdatedAt(),
            updatedCategory.getUpdatedBy()
        );

        log.info("Sending update category message: {}", event);
        kafkaTemplate.send("university.category.updated", event);

        // Convert to response DTO before returning
        return mapToResponse(updatedCategory);
    }

    public void deleteUniversityCategory(Integer id, String username) {
        UniversityCategory category = universityCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University category not found with id: " + id));

        // Soft delete by changing status
        category.setStatus(Status.DELETED);
        category.setUpdatedBy(username);
        category.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        UniversityCategory deletedCategory = universityCategoryRepository.save(category);

        UniversityCategoryEvent event = new UniversityCategoryEvent(
            deletedCategory.getId(),
            deletedCategory.getName(),
            deletedCategory.getDescription(),
            deletedCategory.getStatus(),
            deletedCategory.getCreatedAt(),
            deletedCategory.getCreatedBy(),
            deletedCategory.getUpdatedAt(),
            deletedCategory.getUpdatedBy()
        );

        log.info("Sending delete category message for ID: {}", id);
        kafkaTemplate.send("university.category.deleted", event);
    }

    // Helper method to map entity to response DTO
    private UniversityCategoryResponse mapToResponse(UniversityCategory category) {
        return new UniversityCategoryResponse(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getStatus(),
            category.getCreatedAt(),
            category.getCreatedBy(),
            category.getUpdatedAt(),
            category.getUpdatedBy()
        );
    }
}