package com.example.SBA_M.dto;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractDTO<T extends Serializable> implements Serializable {

    private T id;
    private Status status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

}
