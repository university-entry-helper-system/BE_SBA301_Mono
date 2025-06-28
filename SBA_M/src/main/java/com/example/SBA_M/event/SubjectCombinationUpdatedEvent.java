package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCombinationUpdatedEvent {
    private Long subjectCombinationId;
    private String subjectCombination;
    private List<String> examSubjects;
}
