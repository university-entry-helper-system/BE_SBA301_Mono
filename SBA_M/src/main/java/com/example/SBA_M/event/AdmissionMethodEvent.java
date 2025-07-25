package com.example.SBA_M.event;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdmissionMethodEvent extends AbstractCreatedEvent<Integer>{
    private String name;
    private String description;

    public AdmissionMethodEvent(Integer id, String name, String description, Status status,
                                Instant createdAt,
                                String createdBy,
                                Instant updatedAt,
                                String updatedBy) {
        this.setId(id);
        this.name = name;
        this.description = description;
        this.setStatus(status);
        this.setCreatedBy(createdBy);
        this.setCreatedAt(createdAt);
        this.setUpdatedBy(updatedBy);
        this.setUpdatedAt(updatedAt);
    }
}
