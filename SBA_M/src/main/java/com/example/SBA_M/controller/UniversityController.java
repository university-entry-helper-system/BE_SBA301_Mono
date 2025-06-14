package com.example.SBA_M.controller;

import com.example.SBA_M.entity.University;
import com.example.SBA_M.service.UniversityServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/universities")
public class UniversityController {
    private final UniversityServiceImpl universityService;

    public UniversityController(UniversityServiceImpl universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public List<University> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    @PostMapping
    public University createUniversity(@RequestBody University university) {
        return universityService.saveUniversity(university);
    }
}