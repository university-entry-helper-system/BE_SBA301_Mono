package com.example.SBA_M.entity.queries;

import com.example.SBA_M.utils.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public abstract class AbstractElasticsearchDocument<T extends Serializable> implements Serializable {

    @Field(type = FieldType.Keyword)
    private T id;

    @Field(type = FieldType.Keyword)
    private Status status;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Keyword)
    private String createdBy;

    @Field(type = FieldType.Date)
    private Instant updatedAt;

    @Field(type = FieldType.Keyword)
    private String updatedBy;
}
