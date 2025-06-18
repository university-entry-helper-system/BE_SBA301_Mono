package com.example.SBA_M.entity.queries;

import com.example.SBA_M.entity.commands.University;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "universities")
@EqualsAndHashCode(callSuper = true)
public class UniversityDocument extends AbstractDocument<Integer> {

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

    public UniversityDocument(Integer id, UniversityCategoryDocument categoryDoc, String name, String shortName, String logoUrl, Integer foundingYear, String province, String type, String address, String email, String phone, String website, String description, Instant createdAt, Instant updatedAt) {
        this.setId(id);
        this.category = categoryDoc;
        this.name = name;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
        this.foundingYear = foundingYear;
        this.province = province;
        this.type = type;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.description = description;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }
}