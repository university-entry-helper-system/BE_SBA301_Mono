package com.example.SBA_M.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ConsultationAnswerRequest {

    @NotNull(message = "Consultation ID is required.")
    private Long consultationId;

    @NotBlank(message = "Answer is required.")
    @Size(max = 255, message = "Answer must not exceed 255 characters.")
    private String answer;

    private String resolutionNotes;
}
