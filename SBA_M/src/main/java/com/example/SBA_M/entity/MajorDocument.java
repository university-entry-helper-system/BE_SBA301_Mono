package com.example.SBA_M.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Document(collection = "majors")
@NoArgsConstructor
@AllArgsConstructor
public class MajorDocument {

    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("code")
    @Indexed(unique = true) // Mã ngành là duy nhất
    private String code;

    @Field("major_group")
    private String majorGroup;

    @Field("degree")
    private String degree;

    @Field("description")
    private String description;

    @Field("is_active")
    private Boolean isActive = true;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("(updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}
