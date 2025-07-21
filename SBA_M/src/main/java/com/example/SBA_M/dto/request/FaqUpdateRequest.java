package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.FaqType;
import com.example.SBA_M.utils.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqUpdateRequest {
    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    private FaqType faqType;

    private Status status; // ✅ nên dùng enum luôn
}