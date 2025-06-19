package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.event.UniversityCreatedEvent;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UniversityProducer {
    private final UniversityRepository universityRepository;
    private final KafkaTemplate<String, UniversityCreatedEvent> kafkaTemplate;

    public University createUniversity(UniversityRequest universityRequest, String username) {
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
        uni.setType(universityRequest.getType());
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
        // createdAt and updatedAt are set by default

        University savedUni = universityRepository.save(uni);

        UniversityCreatedEvent event = new UniversityCreatedEvent(
            savedUni.getId(),
            savedUni.getCategory().getId(),
            savedUni.getName(),
            savedUni.getShortName(),
            savedUni.getLogoUrl(),
            savedUni.getFoundingYear(),
            savedUni.getProvince(),
            savedUni.getType(),
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

        kafkaTemplate.send("uni.created", event);
        return savedUni;
    }
}