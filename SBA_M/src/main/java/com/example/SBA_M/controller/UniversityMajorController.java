package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityMajorRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityMajorResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.tuition_search_response.AdmissionUniversityTuitionResponse;
import com.example.SBA_M.service.UniversityMajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/university-majors")
@SecurityRequirement(name = "bearerAuth")
public class UniversityMajorController {

    private final UniversityMajorService universityMajorService;

    public UniversityMajorController(UniversityMajorService universityMajorService) {
        this.universityMajorService = universityMajorService;
    }

    @Operation(summary = "Get all university majors (paginated)", description = "Retrieve paginated list of university majors")
    @GetMapping
    public ResponseEntity<PageResponse<UniversityMajorResponse>> getAllUniversityMajors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<UniversityMajorResponse> result = universityMajorService.getAllUniversityMajors(page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get university major by ID", description = "Retrieve a university major by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<UniversityMajorResponse> getUniversityMajorById(@PathVariable Integer id) {
        UniversityMajorResponse response = universityMajorService.getUniversityMajorById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new university major", description = "Create a university major with request data")
    @PostMapping
    public ResponseEntity<UniversityMajorResponse> createUniversityMajor(
            @Valid @RequestBody UniversityMajorRequest request
    ) {
        UniversityMajorResponse created = universityMajorService.createUniversityMajor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a university major", description = "Update a university major with request data")
    @PutMapping("/{id}")
    public ResponseEntity<UniversityMajorResponse> updateUniversityMajor(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityMajorRequest request
    ) {
        UniversityMajorResponse updated = universityMajorService.updateUniversityMajor(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a university major", description = "Delete a university major by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUniversityMajor(@PathVariable Integer id) {
        universityMajorService.deleteUniversityMajor(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "University major deleted successfully");
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get grouped admissions by university", description = "Get admissions grouped by year > method > major")
    @GetMapping("/admissions/university/{universityId}")
    public ResponseEntity<AdmissionUniversityTuitionResponse> getUniversityAdmissionYears(
            @PathVariable Integer universityId
    ) {
        AdmissionUniversityTuitionResponse response = universityMajorService.getAdmissionYearGroupsByUniversityId(universityId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get grouped admissions by university + major", description = "Get admissions grouped by year > method > subject combination")
    @GetMapping("/admissions/university/{universityId}/major/{majorId}")
    public ResponseEntity<MajorAdmissionResponse> getMajorAdmissionByUniversity(
            @PathVariable Integer universityId,
            @PathVariable Long majorId
    ) {
        MajorAdmissionResponse response = universityMajorService.getMajorAdmissionByUniversityAndMajor(universityId, majorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get grouped admissions by subject combination", description = "Get admissions grouped by year > method > majors")
    @GetMapping("/admissions/university/{universityId}/subject-combination/{subjectCombinationId}")
    public ResponseEntity<SubjectCombinationResponse> getSubjectCombinationAdmission(
            @PathVariable Integer universityId,
            @PathVariable Long subjectCombinationId
    ) {
        SubjectCombinationResponse response = universityMajorService.getSubjectCombinationAdmission(universityId, subjectCombinationId);
        return ResponseEntity.ok(response);
    }
}
