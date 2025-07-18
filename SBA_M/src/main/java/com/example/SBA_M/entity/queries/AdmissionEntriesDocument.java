package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admission_entries")
@CompoundIndexes({
        @CompoundIndex(
                name = "compound_key_index",
                def = "{'university_id': 1, 'major_id': 1, 'method_id': 1, 'subject_combination_id': 1, 'year': 1}"
        ),
        @CompoundIndex(
                name = "university_major_status_year_idx",
                def = "{'university_id': 1, 'major_id': 1, 'status': 1, 'year': -1}"
        ),
        @CompoundIndex(
                name = "university_status_subject_year_idx",
                def = "{'university_id': 1, 'subject_combination_id': 1, 'status': 1, 'year': -1}"
        )
})
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
