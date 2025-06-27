package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.event.AdmissionMethodCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdmissionMethodProducer {

    private final KafkaTemplate<String, AdmissionMethodCreatedEvent> kafkaTemplate;

    public void sendCreateEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.created", buildEvent(am));
    }

    public void sendUpdateEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.updated", buildEvent(am));
    }

    public void sendDeleteEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.deleted", buildEvent(am));
    }

    private AdmissionMethodCreatedEvent buildEvent(AdmissionMethod am) {
        return new AdmissionMethodCreatedEvent(
                am.getId(),
                am.getName(),
                am.getDescription(),
                am.getStatus(),
                am.getCreatedAt(),
                am.getCreatedBy(),
                am.getUpdatedAt(),
                am.getUpdatedBy()
        );
    }
}
