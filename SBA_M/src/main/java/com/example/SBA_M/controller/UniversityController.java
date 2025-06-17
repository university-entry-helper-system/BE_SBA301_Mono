package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/universities")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public List<University> getAllUniversities() {
        return universityService.getAllUniversities();
    }

//    @PostMapping
//    public University createsUniversity(@RequestBody University university) {
//        return universityService.saveUniversity(university);
//    }

    @Operation(summary = "Get university by ID", description = "Retrieve a university document by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<UniversityDocument> getUniversityById(@PathVariable Long id) {
        UniversityDocument university = universityService.getUniversityById(id);
        if (university != null) {
            return ResponseEntity.ok(university);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new university", description = "Create a new university from the provided request data")
    @PostMapping
    public ResponseEntity<University> createUniversity(@RequestBody UniversityRequest universityRequest) {
        University created = universityService.createUniversity(universityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}