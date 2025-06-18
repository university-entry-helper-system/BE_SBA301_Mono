package com.example.SBA_M.entity.queries;

import com.example.SBA_M.entity.commands.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class MajorDocument extends AbstractDocument<String> {

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

    public MajorDocument(String id, String name, String code, String majorGroup, String degree, String description) {
        this.setId(id);
        this.name = name;
        this.code = code;
        this.majorGroup = majorGroup;
        this.degree = degree;
        this.description = description;
    }
}
