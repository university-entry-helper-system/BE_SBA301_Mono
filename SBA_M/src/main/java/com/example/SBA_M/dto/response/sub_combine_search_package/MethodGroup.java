package com.example.SBA_M.dto.response.sub_combine_search_package;

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
public class MethodGroup {
    private String method;
    private String methodName;
    private List<MajorScoreEntry> majors;
}
