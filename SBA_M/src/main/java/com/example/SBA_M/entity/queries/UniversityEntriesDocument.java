package com.example.SBA_M.entity.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "university_entries")
public class UniversityEntriesDocument {

    private Integer universityId;
    private String universityName;
    private Integer majorId;
    private String majorName;
    private Integer methodId;
    private String methodName;
    private Integer subjectCombinationId;
    private String subjectCombination;
    private Double score;
    private String note;
    private Integer year;
}
