package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamSubjectResponse {
    private Long id;
    private String name;
    private String shortName;
    private com.example.SBA_M.utils.Status status;
}
