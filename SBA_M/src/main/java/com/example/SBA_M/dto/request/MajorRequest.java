package com.example.SBA_M.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorRequest {
     String name;
     String code;
     Long majorParentId;
     String degree;
     String description;
     List<Long> subjectCombinationIds;
}
