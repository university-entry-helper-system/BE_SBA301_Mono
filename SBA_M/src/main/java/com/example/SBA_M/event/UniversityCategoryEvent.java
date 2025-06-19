package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UniversityCategoryEvent extends AbstractCreatedEvent<Integer>{
    private String name;
    private String description;
    private List<Long> universityIds;
}