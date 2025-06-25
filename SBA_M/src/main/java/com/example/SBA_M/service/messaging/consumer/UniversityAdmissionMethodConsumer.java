package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.UniversityEntriesDocument;
import com.example.SBA_M.event.UniversityAdmissionMethodEvent;
import com.example.SBA_M.repository.queries.UniversityAdmissionMethodReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniversityAdmissionMethodConsumer {

    private final UniversityAdmissionMethodReadRepository universityEntriesRepository;

    @KafkaListener(topics = "university-admission-method.event", groupId = "sba-group")
    public void consume(UniversityAdmissionMethodEvent event) {

        UniversityEntriesDocument document = new UniversityEntriesDocument(
                event.getId(),
                event.getUniversityId(),
                event.getUniversityName(),
                event.getMethodId(),
                event.getMethodName(),
                event.getYear(),
                event.getNotes(),
                event.getConditions(),
                event.getRegulations(),
                event.getAdmissionTime(),
                event.getStatus()
        );

        universityEntriesRepository.save(document);
    }
}
