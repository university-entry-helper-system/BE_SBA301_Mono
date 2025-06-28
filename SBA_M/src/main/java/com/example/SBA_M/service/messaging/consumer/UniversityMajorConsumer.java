package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.entity.queries.UniversityEntriesDocument;
import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.event.UniversityMajorEventBatch;
import com.example.SBA_M.event.UniversityMajorSearchEventBatch;
import com.example.SBA_M.event.UniversityUpdatedEvent;
import com.example.SBA_M.repository.elasticsearch.UniversityMajorSearchRepository;
import com.example.SBA_M.repository.queries.UniversityAdmissionMethodReadRepository;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniversityMajorConsumer {
    private final UniversityMajorReadRepository universityMajorReadRepository;
    private final UniversityMajorSearchRepository universityMajorSearchRepository;
    private final UniversityAdmissionMethodReadRepository universityAdmissionMethodReadRepository;

    @KafkaListener(topics = "university-major.bulk-event", groupId = "sba-group")
    public void consume(UniversityMajorEventBatch batch) {
        List<AdmissionEntriesDocument> documents = batch.getEvents().stream()
                .map(event -> {
                    AdmissionEntriesDocument doc = new AdmissionEntriesDocument();
                    doc.setId(event.getId());
                    doc.setUniversityId(event.getUniversityId());
                    doc.setUniversityName(event.getUniversityName());
                    doc.setMajorId(event.getMajorId());
                    doc.setMajorName(event.getMajorName());
                    doc.setMethodId(event.getMethodId());
                    doc.setMethodName(event.getMethodName());
                    doc.setSubjectCombinationId(event.getSubjectCombinationId());
                    doc.setSubjectCombination(event.getSubjectCombination());
                    doc.setScore(event.getScore());
                    doc.setNote(event.getNote());
                    doc.setStatus(event.getStatus());
                    return doc;
                })
                .toList();

        universityMajorReadRepository.saveAll(documents);
    }

    @KafkaListener(topics = "university-major-search.event", groupId = "sba-search-group")
    public void consume(UniversityMajorSearchEventBatch event) {
        List<UniversityMajorSearch> documents = event.getEvents().stream()
                .map(e -> {
                    UniversityMajorSearch document = new UniversityMajorSearch();
                    document.setId(e.getId());
                    document.setUniversityId(e.getUniversityId());
                    document.setUniversityName(e.getUniversityName());
                    document.setProvince(e.getProvince());
                    document.setMajorId(e.getMajorId());
                    document.setMajorName(e.getMajorName());
                    document.setSubjectCombinationId(e.getSubjectCombinationId());
                    document.setSubjectCombinationName(e.getSubjectCombinationName());
                    document.setMethods(e.getMethods());
                    document.setUniversityMajorCountByMajor(e.getUniversityMajorCountByMajor());
                    document.setUniversityMajorCountBySubjectCombination(e.getUniversityMajorCountBySubjectCombination());
                    document.setStatus(e.getStatus());
                    return document;
                })
                .toList();


        universityMajorSearchRepository.saveAll(documents);
    }

    @KafkaListener(topics = "university.updated.event", groupId = "sba-search-group")
    public void consumeUniversityUpdate(UniversityUpdatedEvent event) {
        // 1. Update MongoDB Admission Entries
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByUniversityId(event.getUniversityId());
        admissionEntries.forEach(doc -> doc.setUniversityName(event.getUniversityName()));
        universityMajorReadRepository.saveAll(admissionEntries);

        // 2. Update MongoDB University Entries
        List<UniversityEntriesDocument> universityEntries = universityAdmissionMethodReadRepository.findByUniversityId(event.getUniversityId());
        universityEntries.forEach(doc -> doc.setUniversityName(event.getUniversityName()));
        universityAdmissionMethodReadRepository.saveAll(universityEntries);

    }


    @KafkaListener(topics = "university.deleted.event", groupId = "sba-search-group")
    public void consumeUniversityDelete(UniversityUpdatedEvent event) {
        // 1. Delete from MongoDB Admission Entries
        List<AdmissionEntriesDocument> admissionEntries = universityMajorReadRepository.findByUniversityId(event.getUniversityId());
        admissionEntries.forEach((doc) -> doc.setStatus(Status.DELETED)); // Assuming you want to mark as deleted
        universityMajorReadRepository.saveAll(admissionEntries);

        // 2. Delete from MongoDB University Entries
        List<UniversityEntriesDocument> universityEntries = universityAdmissionMethodReadRepository.findByUniversityId(event.getUniversityId());
        universityEntries.forEach((doc) -> doc.setStatus(Status.DELETED)); // Assuming you want to mark as deleted
        universityAdmissionMethodReadRepository.saveAll(universityEntries);

        // 3. Delete from Elasticsearch University Major Search
        List<UniversityMajorSearch> searchDocs = universityMajorSearchRepository.findByUniversityId(event.getUniversityId());
        searchDocs.forEach((doc) -> doc.setStatus(Status.DELETED)); // Assuming you want to mark as deleteduniversityMajorSearchRepository.saveAll(searchDocs);
    }

    @KafkaListener(topics = "university-major.updated.search.event", groupId = "sba-search-group")
    public void consumeUniversityMajorUpdateSearch(UniversityUpdatedEvent event) {
        // Update Elasticsearch University Major Search
        List<UniversityMajorSearch> searchDocs = universityMajorSearchRepository.findByUniversityId(event.getUniversityId());
        searchDocs.forEach(doc -> {
            doc.setUniversityName(event.getUniversityName());
            doc.setProvince(event.getProvince()); // Assuming province name is included in the event
        });
        universityMajorSearchRepository.saveAll(searchDocs);
    }
}

