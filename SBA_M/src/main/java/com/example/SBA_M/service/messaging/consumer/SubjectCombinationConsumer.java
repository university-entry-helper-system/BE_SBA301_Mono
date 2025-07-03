package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.event.MajorUpdatedEvent;
import com.example.SBA_M.event.SubjectCombinationUpdatedEvent;
import com.example.SBA_M.repository.elasticsearch.UniversityMajorSearchRepository;
import com.example.SBA_M.repository.elasticsearch.UniversitySearchRepository;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectCombinationConsumer {
    private final UniversityMajorReadRepository universityMajorReadRepository;
    private final UniversityMajorSearchRepository universityMajorSearchRepository;

    @KafkaListener(topics = "subject_combination.updated", groupId = "sba-group")
    public void consumeSubjectCombinationUpdatedEvent(SubjectCombinationUpdatedEvent event) {
        // Handle the subject combination updated event
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findBySubjectCombinationId(event.getSubjectCombinationId());
        admissionEntries.forEach(doc -> doc.setMajorName(event.getSubjectCombination()));
        universityMajorReadRepository.saveAll(admissionEntries);

        List<UniversityMajorSearch> universityMajorSearches = universityMajorSearchRepository.findBySubjectCombinationId(event.getSubjectCombinationId());
        universityMajorSearches.forEach(doc -> doc.setSubjectCombinationName(event.getSubjectCombination()));
        universityMajorSearchRepository.saveAll(universityMajorSearches);
    }
    @KafkaListener(topics = "subject_combination.deleted", groupId = "sba-group")
    public void consumeSubjectCombinationDeletedEvent(SubjectCombinationUpdatedEvent event) {
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByMajorId(event.getSubjectCombinationId());
        admissionEntries.forEach(doc -> doc.setStatus(Status.DELETED));
        universityMajorReadRepository.saveAll(admissionEntries);

        List<UniversityMajorSearch> universityMajorSearches = universityMajorSearchRepository.findBySubjectCombinationId(event.getSubjectCombinationId());
        universityMajorSearches.forEach(doc -> doc.setStatus(Status.DELETED));
        universityMajorSearchRepository.saveAll(universityMajorSearches);
    }
}
