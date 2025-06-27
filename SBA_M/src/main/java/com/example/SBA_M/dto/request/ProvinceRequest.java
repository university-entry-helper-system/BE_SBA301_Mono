package com.example.SBA_M.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceRequest {
    private String name;
    private String region;
}
