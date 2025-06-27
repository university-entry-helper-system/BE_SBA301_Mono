package com.example.SBA_M.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceResponse {
    private Integer id;
    private String name;
    private String region;
}
