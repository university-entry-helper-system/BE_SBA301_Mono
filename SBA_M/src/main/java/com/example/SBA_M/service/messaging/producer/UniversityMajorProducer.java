package com.example.SBA_M.service.messaging.producer;

import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.commands.ExamSubject;
import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.entity.commands.UniversityMajor;
import com.example.SBA_M.event.UniversityMajorEvent;
import com.example.SBA_M.event.UniversityMajorEventBatch;
import com.example.SBA_M.event.UniversityMajorSearchEvent;
import com.example.SBA_M.dto.response.ComboCountProjection;
import com.example.SBA_M.event.UniversityMajorSearchEventBatch;
import com.example.SBA_M.repository.commands.UniversityMajorRepository;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityMajorProducer {
    private final KafkaTemplate<String, UniversityMajorEventBatch> kafkaTemplate;
    private final KafkaTemplate<String, UniversityMajorSearchEventBatch> kafkaSearchTemplate;

    private final UniversityMajorRepository universityMajorRepository;

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

    public void sendSearchEvent(UniversityMajor universityMajor) {
        Integer universityId = universityMajor.getUniversity().getId();
        Long majorId = universityMajor.getMajor().getId();
        Status status = universityMajor.getStatus();

        // Count by major (only once)
        int countByMajor = universityMajorRepository
                .countByUniversityIdAndMajorIdAndStatus(universityId, majorId, status);

        // Get all subject combination IDs
        List<Long> subjectCombinationIds = universityMajor.getSubjectCombinations().stream()
                .map(SubjectCombination::getId)
                .toList();

        // Get count per subjectCombination in 1 query
        Map<Long, Integer> countByComboMap = universityMajorRepository
                .countByUniversityIdAndSubjectCombinationIds(universityId, subjectCombinationIds, status)
                .stream()
                .collect(Collectors.toMap(
                        ComboCountProjection::getComboId,
                        ComboCountProjection::getCount
                ));



        List<UniversityMajorSearchEvent> events = new ArrayList<>();
        // Send one event per subjectCombination
        for (SubjectCombination combo : universityMajor.getSubjectCombinations()) {

            String id = universityId + "-" + majorId + "-" + combo.getId();


            int countByCombo = countByComboMap.getOrDefault(combo.getId(), 0);

            UniversityMajorSearchEvent event = new UniversityMajorSearchEvent(
                    id,
                    universityId,
                    universityMajor.getUniversity().getName(),
                    universityMajor.getUniversity().getProvince().getName(),
                    majorId,
                    universityMajor.getMajor().getName(),
                    combo.getId(),
                    combo.getName(),
                    countByMajor,
                    countByCombo,
                    status
            );
            events.add(event);
        }
        kafkaSearchTemplate.send("university-major-search.event", new UniversityMajorSearchEventBatch(events));

    }
}
