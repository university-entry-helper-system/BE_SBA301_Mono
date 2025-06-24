package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/majors")
@SecurityRequirement(name = "bearerAuth")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @Operation(summary = "Get all majors")
    @GetMapping
    public ApiResponse<List<Major>> getAllMajors() {
        List<Major> majors = majorService.getAllMajors();
        return ApiResponse.<List<Major>>builder()
                .code(1000)
                .message("List of majors fetched successfully")
                .result(majors)
                .build();
    }

    @Operation(summary = "Create a new major")
    @PostMapping
    public ApiResponse<Major> createMajor(@RequestBody Major major) {
        Major created = majorService.saveMajor(major);
        return ApiResponse.<Major>builder()
                .code(1001)
                .message("Major created successfully")
                .result(created)
                .build();
    }
}
