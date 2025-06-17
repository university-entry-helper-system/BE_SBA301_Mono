package com.example.SBA_M.entity.commands;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubjectCombinationSubjectId implements Serializable {
    private Long subjectCombinationId;
    private Long examSubjectId;
}