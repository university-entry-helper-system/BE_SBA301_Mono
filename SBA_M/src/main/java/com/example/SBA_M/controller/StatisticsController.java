package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get campus statistics by province")
    @GetMapping("/campuses-by-province")
    public ApiResponse<List<Map<String, Object>>> getCampusStatisticsByProvince() {
        List<Map<String, Object>> statistics = statisticsService.getCampusStatisticsByProvince();
        return ApiResponse.<List<Map<String, Object>>>builder()
                .code(1000)
                .message("Campus statistics by province fetched successfully")
                .result(statistics)
                .build();
    }
} 