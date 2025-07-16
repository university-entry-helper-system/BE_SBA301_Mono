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
public class UniversityEvent extends AbstractCreatedEvent<Integer> {
    private Integer categoryId;
    private String name;
    private String shortName;
    private String logoUrl;
    private Integer foundingYear;
    private String email;
    private String phone;
    private String website;
    private String description;
    private Integer admissionMethodId;

    public UniversityEvent(
            Integer id,
            Integer categoryId,
            String name,
            String shortName,
            String logoUrl,
            Integer foundingYear,
            String email,
            String phone,
            String website,
            String description,
            Status status,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy
    ) {
        this.setId(id);
        this.setStatus(status);
        this.setCreatedBy(createdBy);
        this.setCreatedAt(createdAt);
        this.setUpdatedBy(updatedBy);
        this.setUpdatedAt(updatedAt);
        this.categoryId = categoryId;
        this.name = name;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
        this.foundingYear = foundingYear;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.description = description;
    }
    }