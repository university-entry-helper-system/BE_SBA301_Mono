package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "subject_combination_subjects")
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCombinationSubject {

    @EmbeddedId
    private SubjectCombinationSubjectId id;

    @ManyToOne
    @MapsId("subjectCombinationId")
    @JoinColumn(name = "subject_combination_id")
    private SubjectCombination subjectCombination;

    @ManyToOne
    @MapsId("examSubjectId")
    @JoinColumn(name = "exam_subject_id")
    private ExamSubject examSubject;
}