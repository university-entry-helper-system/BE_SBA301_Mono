package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionMethodDocument;
import com.example.SBA_M.event.AdmissionMethodCreatedEvent;
import com.example.SBA_M.repository.queries.AdmissionMethodReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdmissionMethodConsumer {
    private final AdmissionMethodReadRepository admissionMethodReadRepository;
    @KafkaListener(topics = "am.created", groupId = "sba-group")
    public void consume(AdmissionMethodCreatedEvent event) {
        AdmissionMethodDocument doc = new AdmissionMethodDocument(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStatus(),
                event.getCreatedAt(),
                event.getCreatedBy(),
                event.getUpdatedAt(),
                event.getUpdatedBy()
        );
        admissionMethodReadRepository.save(doc);
    }

}
