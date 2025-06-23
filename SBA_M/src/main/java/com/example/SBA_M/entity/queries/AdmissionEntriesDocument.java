package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admission_entries")
public class AdmissionEntriesDocument {

    @Id
    private String id;

    @Field("university_id")
    private Integer universityId;

    @Field("university_name")
    private String universityName;

    @Field("major_id")
    private Long majorId;

    @Field("major_name")
    private String majorName;

    @Field("method_id")
    private Integer methodId;

    @Field("method_name")
    private String methodName;

    @Field("subject_combination_id")
    private Long subjectCombinationId;

    @Field("subject_combination")
    private String subjectCombination;

    @Field("score")
    private Double score;

    @Field("note")
    private String note;

    @Field("year")
    private Integer year;

    @Field("status")
    private Status status;
}
