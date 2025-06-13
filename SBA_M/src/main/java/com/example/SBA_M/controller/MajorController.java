package com.example.SBA_M.controller;

import com.example.SBA_M.entity.Major;
import com.example.SBA_M.service.MajorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/majors")
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