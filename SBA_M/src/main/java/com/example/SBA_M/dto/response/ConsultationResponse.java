package com.example.SBA_M.dto.response;

import com.example.SBA_M.utils.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponse {

    private Long id;

    private SimpleAccountResponse sender;

    private SimpleAccountResponse consultant;

    private String title;

    private String content;

    private String answer;

    private ConsultationStatus consultationsStatus;

    private Instant sentAt;

    private Instant senderUpdatedAt;

    private Instant consultantUpdatedAt;

    private Instant answeredAt;

    private String resolutionNotes;
}
