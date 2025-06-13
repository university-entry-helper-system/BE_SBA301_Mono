package com.example.SBA_M.controller;

import com.example.SBA_M.entity.University;
import com.example.SBA_M.service.UniversityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universities")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
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