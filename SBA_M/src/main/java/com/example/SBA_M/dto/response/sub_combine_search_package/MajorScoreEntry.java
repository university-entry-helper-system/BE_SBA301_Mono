package com.example.SBA_M.dto.response.sub_combine_search_package;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorScoreEntry {
    private String majorId;
    private String majorName;
    private Double score;
    private String note;
}
