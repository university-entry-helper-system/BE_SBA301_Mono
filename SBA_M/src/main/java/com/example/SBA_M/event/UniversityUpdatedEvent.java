package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityUpdatedEvent {
    private Integer universityId;
    private String universityName;
    private String province;
}
