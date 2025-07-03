package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.event.MajorUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorProducer {
    private final KafkaTemplate<String, MajorUpdatedEvent> kafkaTemplate;
    public void sendMajorUpdatedEvent(Long majorId, String majorName) {
        MajorUpdatedEvent event = new MajorUpdatedEvent(majorId, majorName);
        kafkaTemplate.send("major.updated", event);
    }
    public void sendMajorDeletedEvent(Long majorId){
        MajorUpdatedEvent event = new MajorUpdatedEvent(majorId, null);
        kafkaTemplate.send("major.deleted", event);
    }

}
