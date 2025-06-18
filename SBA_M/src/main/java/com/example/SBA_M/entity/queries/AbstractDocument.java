package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractDocument<T extends Serializable> implements Serializable {

    @Id
    @Field("id")
    private T id;

    @Field("status")
    private Status status;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @Field("created_by")
    private String createdBy;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;
}