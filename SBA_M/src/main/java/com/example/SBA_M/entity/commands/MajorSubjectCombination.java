package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "major_subject_combinations")
@NoArgsConstructor
@AllArgsConstructor
public class MajorSubjectCombination {

    @EmbeddedId
    private MajorSubjectCombinationId id;

    @ManyToOne
    @MapsId("majorId")
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @MapsId("subjectCombinationId")
    @JoinColumn(name = "subject_combination_id")
    private SubjectCombination subjectCombination;
}