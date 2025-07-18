package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectCombinationResponse {
    private Long id;
    private String name;
    private String description;
    private List<ExamSubjectResponse> examSubjects;
    private com.example.SBA_M.utils.Status status;
    private BlockInfo block;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BlockInfo {
        private Long id;
        private String name;
    }
}
