package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MajorEvent extends AbstractCreatedEvent<Integer>{
    private String name;
    private String code;
    private String majorGroup;
    private String degree;
    private String description;
}