package com.example.SBA_M.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponse {
    private Long id;
    private String question;
    private String answer;
}