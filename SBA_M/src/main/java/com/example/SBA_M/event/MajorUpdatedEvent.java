package com.example.SBA_M.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorUpdatedEvent {
    private Long majorId;
    private String majorName;
}
