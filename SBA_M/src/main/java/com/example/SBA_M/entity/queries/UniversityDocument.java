package com.example.SBA_M.entity.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "universities")
public class UniversityDocument {

    @Id
    @Field("id")
    private Long id;

    @Field("category")
    private UniversityCategoryDocument category;

    @Field("name")
    private String name;

    @Field("short_name")
    private String shortName;

    @Field("logo_url")
    private String logoUrl;

    @Field("founding_year")
    private Integer foundingYear;

    @Field("province")
    private String province;

    @Field("type")
    private String type;

    @Field("address")
    private String address;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("website")
    private String website;

    @Field("description")
    private String description;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}