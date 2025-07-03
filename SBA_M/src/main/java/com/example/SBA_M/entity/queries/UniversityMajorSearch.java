package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "university_major_search")
@Setting(settingPath = "/elasticsearch/university-settings.json")
public class UniversityMajorSearch {

    @Id
    private String id; // Composite key: universityId-majorId-subjectCombinationId

    @Field(type = FieldType.Integer)
    private Integer universityId;

    @Field(type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "standard")
    private String universityName;

    @Field(type = FieldType.Keyword)
    private String province; // For filtering

    @Field(type = FieldType.Integer)
    private Long majorId;

    @Field(type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "standard")
    private String majorName;

    @Field(type = FieldType.Long)
    private Long subjectCombinationId;

    @Field(type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "standard")
    private String subjectCombinationName;

    @Field(type = FieldType.Integer)
    private Integer universityMajorCountByMajor; // Number of university majors by major (at this university)

    @Field(type = FieldType.Integer)
    private Integer universityMajorCountBySubjectCombination; // By subject combo

    @Field(type = FieldType.Boolean)
    private Status status;
}