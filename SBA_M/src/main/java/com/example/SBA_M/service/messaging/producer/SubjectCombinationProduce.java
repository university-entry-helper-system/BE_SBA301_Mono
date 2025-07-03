package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.event.SubjectCombinationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectCombinationProduce {
    private final KafkaTemplate<String, SubjectCombinationUpdatedEvent> kafkaTemplate;
    public void sendSubjectCombinationUpdatedEvent(Long subjectCombinationId, String subjectCombinationName) {
        SubjectCombinationUpdatedEvent event = new SubjectCombinationUpdatedEvent(subjectCombinationId, subjectCombinationName);
        kafkaTemplate.send("subject_combination.updated", event);
    }
    public void sendSubjectCombinationDeletedEvent(Long subjectCombinationId) {
        SubjectCombinationUpdatedEvent event = new SubjectCombinationUpdatedEvent(subjectCombinationId, null);
        kafkaTemplate.send("subject_combination.deleted", event);
    }
}
