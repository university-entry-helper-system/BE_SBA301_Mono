package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectCombinationResponse {
    private Long id;
    private String name;
    private String description;
    private List<ExamSubjectResponse> examSubjects;
}
