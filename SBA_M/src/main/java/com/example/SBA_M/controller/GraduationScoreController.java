package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.GraduationScoreRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.GraduationScoreResponse;
import com.example.SBA_M.service.GraduationScoreService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/graduation-score")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GraduationScoreController {

    GraduationScoreService graduationScoreService;

    @Operation(summary = "Calculate graduation score")
    @PostMapping("/calculate")
    public ApiResponse<GraduationScoreResponse> calculateGraduationScore(
            @RequestBody @Valid GraduationScoreRequest request) {
        log.info("Received request to calculate graduation score");
        return ApiResponse.<GraduationScoreResponse>builder()
                .code(1000)
                .message("Graduation score calculated successfully")
                .result(graduationScoreService.calculateGraduationScore(request))
                .build();
    }
}