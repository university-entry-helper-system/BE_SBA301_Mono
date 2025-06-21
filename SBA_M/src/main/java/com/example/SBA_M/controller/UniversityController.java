package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/universities")
@SecurityRequirement(name = "bearerAuth")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @Operation(summary = "Get university by ID", description = "Retrieve a university document by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<UniversityDocument> getUniversityById(@PathVariable Integer id) {
        UniversityDocument university = universityService.getUniversityById(id);
        if (university != null) {
            return ResponseEntity.ok(university);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new university", description = "Create a new university from the provided request data")
    @PostMapping
    public ResponseEntity<UniversityResponse> createUniversity(@RequestBody UniversityRequest universityRequest) {
        UniversityResponse created = universityService.createUniversity(universityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a university", description = "Update an existing university with the provided request data")
    @PutMapping("/{id}")
    public ResponseEntity<UniversityResponse> updateUniversity(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityRequest universityRequest) {
        UniversityResponse updated = universityService.updateUniversity(id, universityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @Operation(summary = "Delete a university", description = "Delete a university by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "University deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}