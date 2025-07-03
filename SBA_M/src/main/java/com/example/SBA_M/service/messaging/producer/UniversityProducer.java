package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.event.UniversityEvent;
import com.example.SBA_M.event.UniversityUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUniversityCreated(University university) {
        UniversityEvent event = buildEvent(university);
        log.info("Sending university created event: {}", event);
        kafkaTemplate.send("uni.created", event);
    }

    public void sendUniversityUpdated(University university) {
        UniversityEvent event = buildEvent(university);
        log.info("Sending university updated event: {}", event);
        kafkaTemplate.send("uni.updated", event);
    }

    public void sendUniversityDeleted(University university) {
        UniversityEvent event = buildEvent(university);
        log.info("Sending university deleted event: {}", event);
        kafkaTemplate.send("uni.deleted", event);
    }

    private UniversityEvent buildEvent(University university) {
        return new UniversityEvent(
                university.getId(),
                university.getCategory().getId(),
                university.getName(),
                university.getShortName(),
                university.getLogoUrl(),
                university.getFoundingYear(),
                university.getProvince().getId(),
                university.getAddress(),
                university.getEmail(),
                university.getPhone(),
                university.getWebsite(),
                university.getDescription(),
                university.getStatus(),
                university.getCreatedAt(),
                university.getCreatedBy(),
                university.getUpdatedAt(),
                university.getUpdatedBy()
        );
    }
    public void sendUpdateEvent(University university) {
        UniversityUpdatedEvent event = new UniversityUpdatedEvent(
                university.getId(),
                university.getName(),
                null
        );
        kafkaTemplate.send("university.updated.event", event);
    }
    public void sendUpdateSearchEvent(University university) {
        UniversityUpdatedEvent event = new UniversityUpdatedEvent(
                university.getId(),
                university.getName(),
                university.getProvince().getName() // Assuming name is not needed for province update event
        );
        kafkaTemplate.send("university.updated.search.event", event);
    }
    public void sendDeleteEvent(Integer id) {
        UniversityUpdatedEvent event = new UniversityUpdatedEvent(
                id,
                null,
                null// Assuming name is not needed for delete event
        );
        kafkaTemplate.send("university.deleted.event", event);
    }
}
