package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.entity.queries.AdmissionMethodDocument;
import com.example.SBA_M.entity.queries.UniversityEntriesDocument;
import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.event.AdmissionMethodEvent;
import com.example.SBA_M.repository.elasticsearch.UniversityMajorSearchRepository;
import com.example.SBA_M.repository.queries.AdmissionMethodReadRepository;
import com.example.SBA_M.repository.queries.UniversityAdmissionMethodReadRepository;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdmissionMethodConsumer {
    private final AdmissionMethodReadRepository admissionMethodReadRepository;
    private final UniversityMajorReadRepository universityMajorReadRepository;
    private final UniversityAdmissionMethodReadRepository universityAdmissionMethodReadRepository;
    @KafkaListener(topics = "am.created", groupId = "sba-group")
    public void consume(AdmissionMethodEvent event) {
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

    @KafkaListener(topics = "am.updated.event", groupId = "sba-group")
    public void consumeUpdate(AdmissionMethodEvent event) {
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByMethodId((event.getId()));
        admissionEntries.forEach((doc) -> doc.setMethodName(event.getName()));
        universityMajorReadRepository.saveAll(admissionEntries);

        List<UniversityEntriesDocument> universityEntries = universityAdmissionMethodReadRepository.findByMethodId(event.getId());
        universityEntries.forEach(doc -> doc.setUniversityName(event.getName()));
        universityAdmissionMethodReadRepository.saveAll(universityEntries);
    }

    @KafkaListener(topics = "am.deleted.event", groupId = "sba-group")
    public void consumeDelete(AdmissionMethodEvent event) {
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByMethodId(event.getId());
        admissionEntries.forEach(doc -> doc.setStatus(Status.DELETED));
        universityMajorReadRepository.saveAll(admissionEntries);

        List<UniversityEntriesDocument> universityEntries = universityAdmissionMethodReadRepository.findByMethodId(event.getId());
        universityEntries.forEach(doc -> doc.setStatus(Status.DELETED));
        universityAdmissionMethodReadRepository.saveAll(universityEntries);
    }
}
