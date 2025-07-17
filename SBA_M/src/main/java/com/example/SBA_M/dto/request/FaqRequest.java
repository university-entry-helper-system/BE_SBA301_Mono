package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.FaqType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqRequest {

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Answer is required")
    private String answer;
    private FaqType faqType;
}