package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.event.MajorUpdatedEvent;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorConsumer {
    private final UniversityMajorReadRepository universityMajorReadRepository;

    @KafkaListener(topics = "major.updated", groupId = "sba-group")
    public void consumeMajorUpdatedEvent(MajorUpdatedEvent event) {
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByMajorId(event.getMajorId());
        admissionEntries.forEach(doc -> doc.setMajorName(event.getMajorName()));
        universityMajorReadRepository.saveAll(admissionEntries);
        // Handle the major updated event
    }
    @KafkaListener(topics = "major.deleted", groupId = "sba-group")
    public void consumeMajorDeletedEvent(MajorUpdatedEvent event) {
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByMajorId(event.getMajorId());
        admissionEntries.forEach(doc -> doc.setStatus(Status.DELETED));
        universityMajorReadRepository.saveAll(admissionEntries);
    }
}
