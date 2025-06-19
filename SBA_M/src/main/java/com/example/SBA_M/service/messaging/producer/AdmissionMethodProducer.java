package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.event.AdmissionMethodCreatedEvent;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AdmissionMethodProducer {
    private final AdmissionMethodRepository admissionMethodRepository;
    private final KafkaTemplate<String, AdmissionMethodCreatedEvent> kafkaTemplate;

    public AdmissionMethod createAdmissionMethod(AdmissionMethodRequest admissionMethodRequest, String username) {
        // Assuming you have a method to get UniversityCategory by ID
        AdmissionMethod admissionMethod = new AdmissionMethod();
        admissionMethod.setName(admissionMethodRequest.getName());
        admissionMethod.setDescription(admissionMethodRequest.getDescription());
        admissionMethod.setStatus(Status.ACTIVE);
        admissionMethod.setCreatedBy(username);
        admissionMethod.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        admissionMethod.setUpdatedBy(username);
        admissionMethod.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
        AdmissionMethod saveAM = admissionMethodRepository.save(admissionMethod);

        AdmissionMethodCreatedEvent event = new AdmissionMethodCreatedEvent(
                saveAM.getId(),
                saveAM.getName(),
                saveAM.getDescription(),
                saveAM.getStatus(),
                saveAM.getCreatedAt(),
                saveAM.getCreatedBy(),
                saveAM.getUpdatedAt(),
                saveAM.getUpdatedBy()
        );

        kafkaTemplate.send("am.created", event);
        return saveAM;
    }

    public void sendUpdateEvent(AdmissionMethod am) {
        AdmissionMethodCreatedEvent event = new AdmissionMethodCreatedEvent(
                am.getId(),
                am.getName(),
                am.getDescription(),
                am.getStatus(),
                am.getCreatedAt(),
                am.getCreatedBy(),
                am.getUpdatedAt(),
                am.getUpdatedBy()
        );
        kafkaTemplate.send("am.created", event);
    }

    public void sendDeleteEvent(AdmissionMethod am) {
        AdmissionMethodCreatedEvent event = new AdmissionMethodCreatedEvent(
                am.getId(),
                am.getName(),
                am.getDescription(),
                am.getStatus(),
                am.getCreatedAt(),
                am.getCreatedBy(),
                am.getUpdatedAt(),
                am.getUpdatedBy()
        );
        kafkaTemplate.send("am.created", event);

    }
}
