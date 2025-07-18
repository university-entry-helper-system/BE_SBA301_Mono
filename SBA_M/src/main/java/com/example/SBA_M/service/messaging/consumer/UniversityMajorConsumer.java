package com.example.SBA_M.service.messaging.consumer;

import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.event.UniversityMajorEventBatch;
import com.example.SBA_M.event.UniversityMajorSearchEventBatch;
import com.example.SBA_M.repository.elasticsearch.UniversityMajorSearchRepository;
import com.example.SBA_M.repository.queries.UniversityAdmissionMethodReadRepository;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
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
                    doc.setYear(event.getYear());
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
                    document.setMajorId(e.getMajorId());
                    document.setMajorName(e.getMajorName());
                    document.setSubjectCombinationId(e.getSubjectCombinationId());
                    document.setSubjectCombinationName(e.getSubjectCombinationName());
                    document.setUniversityMajorCountByMajor(e.getUniversityMajorCountByMajor());
                    document.setUniversityMajorCountBySubjectCombination(e.getUniversityMajorCountBySubjectCombination());
                    document.setStatus(e.getStatus());
                    return document;
                })
                .toList();


        universityMajorSearchRepository.saveAll(documents);
    }

}

