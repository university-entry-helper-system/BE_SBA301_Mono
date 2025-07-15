package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.event.FaqEvent;
import com.example.SBA_M.repository.commands.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqProducer {
    private final FaqRepository faqRepository;
    private final KafkaTemplate<String, FaqEvent> kafkaTemplate;

    public void sendFaqCreatedEvent(Faq faq) {
        FaqEvent event = mapToFaqEvent(faq);
        log.info("Sending FAQ created event: {}", event);
        kafkaTemplate.send("faq.created", event);
    }

    public void sendFaqUpdatedEvent(Faq faq) {
        FaqEvent event = mapToFaqEvent(faq);
        log.info("Sending FAQ updated event: {}", event);
        kafkaTemplate.send("faq.updated", event);
    }

    public void sendFaqDeletedEvent(Faq faq) {
        FaqEvent event = mapToFaqEvent(faq);
        log.info("Sending FAQ deleted event: {}", event);
        kafkaTemplate.send("faq.deleted", event);
    }

    private FaqEvent mapToFaqEvent(Faq faq) {
        return new FaqEvent(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCreatedAt(),
                faq.getCreatedBy(),
                faq.getUpdatedAt(),
                faq.getUpdatedBy(),
                faq.getStatus() != null ? faq.getStatus().name() : null
        );
    }
}