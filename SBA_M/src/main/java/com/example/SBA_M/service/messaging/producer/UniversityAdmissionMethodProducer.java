package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import com.example.SBA_M.event.UniversityAdmissionMethodEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityAdmissionMethodProducer {

    private final KafkaTemplate<String, UniversityAdmissionMethodEvent> kafkaTemplate;

    public void sendCreateEvent(UniversityAdmissionMethod entity) {
        String id = entity.getId() + "-" + entity.getUniversity().getId() + "-" + entity.getAdmissionMethod().getId();
        UniversityAdmissionMethodEvent event = new UniversityAdmissionMethodEvent(
                id,
                entity.getUniversity().getId(),
                entity.getUniversity().getName(),

                entity.getAdmissionMethod().getId(),
                entity.getAdmissionMethod().getName(),
                entity.getYear(),
                entity.getNotes(),
                entity.getConditions(),
                entity.getRegulations(),
                entity.getAdmissionTime(),
                entity.getStatus()
        );

        kafkaTemplate.send("university-admission-method.event", event);
    }
}
