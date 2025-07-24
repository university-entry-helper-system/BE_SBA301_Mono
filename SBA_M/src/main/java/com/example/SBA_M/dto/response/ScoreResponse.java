package com.example.SBA_M.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponse {
    private Long id;
    private String subject;
    private Integer year;
    private String scoreUrl;
    private String type;
}