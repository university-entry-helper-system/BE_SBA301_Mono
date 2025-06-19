package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admission_methods")
@EqualsAndHashCode(callSuper = true)
public class AdmissionMethodDocument extends AbstractDocument<Integer>{

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    public AdmissionMethodDocument(Integer id, String name, String description,
                                   Status status,
                                   Instant createdAt,
                                   String createdBy,
                                   Instant updatedAt,
                                   String updatedBy) {
        this.setId(id);
        this.name = name;
        this.description = description;
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setCreatedBy(createdBy);
        this.setUpdatedAt(updatedAt);
        this.setUpdatedBy(updatedBy);
    }

}