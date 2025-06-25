package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityMajorRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityMajorResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.tuition_search_response.AdmissionUniversityTuitionResponse;
import com.example.SBA_M.service.UniversityMajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/university-majors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UniversityMajorController {

    private final UniversityMajorService universityMajorService;

    @Operation(summary = "Get all university majors (paginated)")
    @GetMapping
    public ApiResponse<PageResponse<UniversityMajorResponse>> getAllUniversityMajors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<UniversityMajorResponse> result = universityMajorService.getAllUniversityMajors(page, size);
        return ApiResponse.<PageResponse<UniversityMajorResponse>>builder()
                .code(1000)
                .message("University majors fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get university major by ID")
    @GetMapping("/{id}")
    public ApiResponse<UniversityMajorResponse> getUniversityMajorById(@PathVariable Integer id) {
        UniversityMajorResponse response = universityMajorService.getUniversityMajorById(id);
        return ApiResponse.<UniversityMajorResponse>builder()
                .code(1000)
                .message("University major fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Create a new university major")
    @PostMapping
    public ApiResponse<UniversityMajorResponse> createUniversityMajor(
            @Valid @RequestBody UniversityMajorRequest request
    ) {
        UniversityMajorResponse created = universityMajorService.createUniversityMajor(request);
        return ApiResponse.<UniversityMajorResponse>builder()
                .code(1001)
                .message("University major created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a university major")
    @PutMapping("/{id}")
    public ApiResponse<UniversityMajorResponse> updateUniversityMajor(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityMajorRequest request
    ) {
        UniversityMajorResponse updated = universityMajorService.updateUniversityMajor(id, request);
        return ApiResponse.<UniversityMajorResponse>builder()
                .code(1002)
                .message("University major updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a university major")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUniversityMajor(@PathVariable Integer id) {
        universityMajorService.deleteUniversityMajor(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("University major deleted successfully")
                .build();
    }

    @Operation(summary = "Get grouped admissions by university")
    @GetMapping("/admissions/university/{universityId}")
    public ApiResponse<AdmissionUniversityTuitionResponse> getUniversityAdmissionYears(
            @PathVariable Integer universityId
    ) {
        AdmissionUniversityTuitionResponse response = universityMajorService.getAdmissionYearGroupsByUniversityId(universityId);
        return ApiResponse.<AdmissionUniversityTuitionResponse>builder()
                .code(1000)
                .message("Admission data grouped by university fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get grouped admissions by university + major")
    @GetMapping("/admissions/university/{majorId}/major/{universityId}")
    public ApiResponse<MajorAdmissionResponse> getMajorAdmissionByUniversity(
            @PathVariable Integer universityId,
            @PathVariable Long majorId
    ) {
        MajorAdmissionResponse response = universityMajorService.getMajorAdmissionByUniversityAndMajor(universityId, majorId);
        return ApiResponse.<MajorAdmissionResponse>builder()
                .code(1000)
                .message("Admission data grouped by major fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get grouped admissions by subject combination")
    @GetMapping("/admissions/university/{subjectCombinationId}/subject-combination/{universityId}")
    public ApiResponse<SubjectCombinationResponse> getSubjectCombinationAdmission(
            @PathVariable Integer universityId,
            @PathVariable Long subjectCombinationId
    ) {
        SubjectCombinationResponse response = universityMajorService.getSubjectCombinationAdmission(universityId, subjectCombinationId);
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1000)
                .message("Admission data grouped by subject combination fetched successfully")
                .result(response)
                .build();
    }
}
