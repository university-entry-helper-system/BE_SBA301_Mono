package com.example.SBA_M.entity.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "university_categories")
@EqualsAndHashCode(callSuper = true)
public class UniversityCategoryDocument extends AbstractDocument<Integer> {

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    public UniversityCategoryDocument(Integer id, String name, String description) {
        this.setId(id);
        this.name = name;
        this.description = description;
    }
}