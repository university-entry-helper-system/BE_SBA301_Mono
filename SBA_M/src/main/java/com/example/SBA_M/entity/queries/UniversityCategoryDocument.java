package com.example.SBA_M.entity.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "university_categories")
public class UniversityCategoryDocument {

    @Id
    @Field("id")
    private Long id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;


}