package com.example.SBA_M.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorResponse {
    Long id;
    String name;
    String code;
    String degree;
    String description;
    MajorParentResponse majorParent;
    List<SubjectCombinationResponse> subjectCombinations;
}