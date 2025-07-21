package com.example.SBA_M.dto.response;

import com.example.SBA_M.utils.FaqType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FaqResponse {
    private Long id;
    private String question;
    private String answer;
    private FaqType faqType;
    private String status;
}