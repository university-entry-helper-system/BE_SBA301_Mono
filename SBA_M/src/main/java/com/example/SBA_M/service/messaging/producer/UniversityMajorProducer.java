package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.entity.commands.UniversityMajor;
import com.example.SBA_M.event.UniversityMajorEvent;
import com.example.SBA_M.event.UniversityMajorEventBatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityMajorProducer {
    private final KafkaTemplate<String, UniversityMajorEventBatch> kafkaTemplate;
    public void sendCreateEvents(UniversityMajor um) {
        var university = um.getUniversity();
        var major = um.getMajor();
        var subjectCombinations = um.getSubjectCombinations();
        var admissionMethods = um.getAdmissionMethods();

        if (subjectCombinations == null || admissionMethods == null) return;

        List<UniversityMajorEvent> events = new ArrayList<>();

        for (AdmissionMethod method : admissionMethods) {
            for (SubjectCombination combo : subjectCombinations) {
                String id = university.getId() + "-" + major.getId() + "-" + method.getId() + "-" + combo.getId();
                UniversityMajorEvent event = new UniversityMajorEvent(
                        id,
                        university.getId(),
                        university.getName(),
                        major.getId(),
                        um.getUniversityMajorName(),
                        method.getId(),
                        method.getName(),
                        combo.getId(),
                        combo.getName(),
                        um.getScore(),
                        um.getNotes(),
                        um.getStatus()
                );
                events.add(event);
            }
        }

        kafkaTemplate.send("university-major.bulk-event", new UniversityMajorEventBatch(events));
    }

}
