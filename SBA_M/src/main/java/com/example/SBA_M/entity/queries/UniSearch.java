package com.example.SBA_M.entity.queries;

import com.example.SBA_M.entity.commands.Province;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "university_search")
@EqualsAndHashCode(callSuper = true)
public class UniSearch extends AbstractElasticsearchDocument<Integer> {

    @Field(type = FieldType.Nested)
    private UniCategorySearch category;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String shortName;

    @Field(type = FieldType.Keyword)
    private String logoUrl;

    @Field(type = FieldType.Integer)
    private Integer foundingYear;
    
    @Field(type = FieldType.Keyword)
    private Province province;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String phone;

    @Field(type = FieldType.Keyword)
    private String website;

    @Field(type = FieldType.Text)
    private String description;
}