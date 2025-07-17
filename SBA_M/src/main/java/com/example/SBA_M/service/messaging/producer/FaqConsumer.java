package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.event.FaqEvent;
import com.example.SBA_M.repository.commands.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FaqConsumer {
    private final FaqRepository faqRepository;

    @KafkaListener(topics = "faq.created", groupId = "sba-group")
    public void consumeFaqCreated(FaqEvent event) {
        log.info("Consumed FAQ created event: {}", event);
        // Handle the creation logic (e.g., save to database)
    }

    @KafkaListener(topics = "faq.updated", groupId = "sba-group")
    public void consumeFaqUpdated(FaqEvent event) {
        log.info("Consumed FAQ updated event: {}", event);
        // Handle the update logic (e.g., update in database)
    }

    @KafkaListener(topics = "faq.deleted", groupId = "sba-group")
    public void consumeFaqDeleted(FaqEvent event) {
        log.info("Consumed FAQ deleted event: {}", event);
        // Handle the deletion logic (e.g., mark as deleted in database)
    }
}