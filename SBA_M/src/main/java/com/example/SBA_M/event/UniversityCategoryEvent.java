package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UniversityCategoryEvent {
    private Integer id;
    private String name;
    private String description;
    private List<Long> universityIds;
}