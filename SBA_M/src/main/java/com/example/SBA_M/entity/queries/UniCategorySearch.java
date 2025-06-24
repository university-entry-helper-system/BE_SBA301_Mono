package com.example.SBA_M.entity.queries;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "university_category_search")
@EqualsAndHashCode(callSuper = true)
public class UniCategorySearch extends AbstractElasticsearchDocument<Integer> {

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;
}