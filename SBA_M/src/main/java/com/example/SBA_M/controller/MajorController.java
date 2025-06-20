package com.example.SBA_M.controller;

import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.service.MajorService;
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

    @GetMapping
    public List<Major> getAllMajors() {
        return majorService.getAllMajors();
    }

    @PostMapping
    public Major createMajor(@RequestBody Major major) {
        return majorService.saveMajor(major);
    }
}