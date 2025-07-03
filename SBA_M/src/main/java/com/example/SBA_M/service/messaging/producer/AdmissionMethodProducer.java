package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.event.AdmissionMethodEvent;
import com.example.SBA_M.event.AdmissionMethodUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdmissionMethodProducer {

    private final KafkaTemplate<String, AdmissionMethodEvent> kafkaTemplate;
    private final KafkaTemplate<String, AdmissionMethodUpdatedEvent> kafkaUpdateTemplate;

    public void sendCreateEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.created", buildEvent(am));
    }

    public void sendUpdateEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.updated", buildEvent(am));
    }

    public void sendDeleteEvent(AdmissionMethod am) {
        kafkaTemplate.send("am.deleted", buildEvent(am));
    }

    private AdmissionMethodEvent buildEvent(AdmissionMethod am) {
        return new AdmissionMethodEvent(
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
    public void sendUpdatedEvent(AdmissionMethod am) {
        AdmissionMethodUpdatedEvent event =  new AdmissionMethodUpdatedEvent(
                am.getId(),
                am.getName()
        );
        kafkaUpdateTemplate.send("am.updated.event", event);
    }
    public void sendDeletedEvent(AdmissionMethod am) {
        AdmissionMethodUpdatedEvent event = new AdmissionMethodUpdatedEvent(
                am.getId(),
                am.getName()
        );
        kafkaUpdateTemplate.send("am.deleted.event", event);
    }
}
