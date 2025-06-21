package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.event.UniversityEvent;
import com.example.SBA_M.repository.commands.UniversityRepository;
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
public class UniversityProducer {
    private final UniversityRepository universityRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UniversityResponse createUniversity(UniversityRequest universityRequest, String username) {
        // Assuming you have a method to get UniversityCategory by ID
        UniversityCategory category = new UniversityCategory();
        category.setId(universityRequest.getCategoryId());
        University uni = new University();
        uni.setCategory(category);
        uni.setName(universityRequest.getName());
        uni.setShortName(universityRequest.getShortName());
        uni.setLogoUrl(universityRequest.getLogoUrl());
        uni.setFoundingYear(universityRequest.getFoundingYear());
        uni.setProvince(universityRequest.getProvince());
        uni.setAddress(universityRequest.getAddress());
        uni.setEmail(universityRequest.getEmail());
        uni.setPhone(universityRequest.getPhone());
        uni.setWebsite(universityRequest.getWebsite());
        uni.setDescription(universityRequest.getDescription());
        uni.setStatus(Status.ACTIVE); // Default status
        uni.setCreatedBy(username);
        uni.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        uni.setUpdatedBy(username);
        uni.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        University savedUni = universityRepository.save(uni);

        UniversityEvent event = new UniversityEvent(
            savedUni.getId(),
            savedUni.getCategory().getId(),
            savedUni.getName(),
            savedUni.getShortName(),
            savedUni.getLogoUrl(),
            savedUni.getFoundingYear(),
            savedUni.getProvince(),
            savedUni.getAddress(),
            savedUni.getEmail(),
            savedUni.getPhone(),
            savedUni.getWebsite(),
            savedUni.getDescription(),
            savedUni.getStatus(),
            savedUni.getCreatedAt(),
            savedUni.getCreatedBy(),
            savedUni.getUpdatedAt(),
            savedUni.getUpdatedBy()
        );

        log.info("Sending university created event: {}", event);
        kafkaTemplate.send("uni.created", event);
        return mapToResponse(savedUni);
    }

    public UniversityResponse updateUniversity(Integer id, UniversityRequest universityRequest, String username) {
        University existingUni = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found with id: " + id));

        // Update university fields
        UniversityCategory category = new UniversityCategory();
        category.setId(universityRequest.getCategoryId());

        existingUni.setCategory(category);
        existingUni.setName(universityRequest.getName());
        existingUni.setShortName(universityRequest.getShortName());
        existingUni.setLogoUrl(universityRequest.getLogoUrl());
        existingUni.setFoundingYear(universityRequest.getFoundingYear());
        existingUni.setProvince(universityRequest.getProvince());
        existingUni.setAddress(universityRequest.getAddress());
        existingUni.setEmail(universityRequest.getEmail());
        existingUni.setPhone(universityRequest.getPhone());
        existingUni.setWebsite(universityRequest.getWebsite());
        existingUni.setDescription(universityRequest.getDescription());
        existingUni.setUpdatedBy(username);
        existingUni.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        University updatedUni = universityRepository.save(existingUni);

        // Create and send update event
        UniversityEvent event = new UniversityEvent(
                updatedUni.getId(),
                updatedUni.getCategory().getId(),
                updatedUni.getName(),
                updatedUni.getShortName(),
                updatedUni.getLogoUrl(),
                updatedUni.getFoundingYear(),
                updatedUni.getProvince(),
                updatedUni.getAddress(),
                updatedUni.getEmail(),
                updatedUni.getPhone(),
                updatedUni.getWebsite(),
                updatedUni.getDescription(),
                updatedUni.getStatus(),
                updatedUni.getCreatedAt(),
                updatedUni.getCreatedBy(),
                updatedUni.getUpdatedAt(),
                updatedUni.getUpdatedBy()
        );

        log.info("Sending university updated event: {}", event);
        kafkaTemplate.send("uni.updated", event);
        return mapToResponse(updatedUni);
    }

    public void deleteUniversity(Integer id, String username) {
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found with id: " + id));

        // Soft delete by changing status
        university.setStatus(Status.DELETED);
        university.setUpdatedBy(username);
        university.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        University deletedUni = universityRepository.save(university);

        // Create and send delete event
        UniversityEvent event = new UniversityEvent(
                deletedUni.getId(),
                deletedUni.getCategory().getId(),
                deletedUni.getName(),
                deletedUni.getShortName(),
                deletedUni.getLogoUrl(),
                deletedUni.getFoundingYear(),
                deletedUni.getProvince(),
                deletedUni.getAddress(),
                deletedUni.getEmail(),
                deletedUni.getPhone(),
                deletedUni.getWebsite(),
                deletedUni.getDescription(),
                deletedUni.getStatus(),
                deletedUni.getCreatedAt(),
                deletedUni.getCreatedBy(),
                deletedUni.getUpdatedAt(),
                deletedUni.getUpdatedBy()
        );

        log.info("Sending university deleted event for ID: {}", id);
        kafkaTemplate.send("uni.deleted", event);
    }

    // Helper method to map entity to response DTO
    private UniversityResponse mapToResponse(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .categoryId(university.getCategory().getId())
                .name(university.getName())
                .shortName(university.getShortName())
                .logoUrl(university.getLogoUrl())
                .foundingYear(university.getFoundingYear())
                .province(university.getProvince())
                .address(university.getAddress())
                .email(university.getEmail())
                .phone(university.getPhone())
                .website(university.getWebsite())
                .description(university.getDescription())
                .status(university.getStatus())
                .createdAt(university.getCreatedAt())
                .createdBy(university.getCreatedBy())
                .updatedAt(university.getUpdatedAt())
                .updatedBy(university.getUpdatedBy())
                .build();
    }
}