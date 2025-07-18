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
@Document(collection = "university_entries")
@CompoundIndexes({
        @CompoundIndex(name = "university_status_year_idx", def = "{'university_id': 1, 'status': 1, 'year': -1}"),
        @CompoundIndex(name = "method_status_year_idx", def = "{'method_id': 1, 'status': 1, 'year': -1}")
})
public class UniversityEntriesDocument {

    @Id
    @Field("_id")
    private String id;

    @Field("university_id")
    private Integer universityId;

    @Field("university_name")
    private String universityName;

    @Field("method_id")
    private Integer methodId;

    @Field("method_name")
    private String methodName;

    @Field("year")
    private Integer year;

    @Field("notes")
    private String notes;

    @Field("conditions")
    private String conditions;

    @Field("regulations")
    private String regulations;

    @Field("admission_time")
    private String admissionTime;

    @Field("status")
    private Status status;
}
