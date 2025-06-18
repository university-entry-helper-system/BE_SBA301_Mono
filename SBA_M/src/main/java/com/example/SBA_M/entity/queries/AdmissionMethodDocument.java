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
@Document(collection = "admission_methods")
public class AdmissionMethodDocument {

    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("short_name")
    private String shortName;

    @Field("description")
    private String description;

    @Field("status")
    private String status = "Active";
}