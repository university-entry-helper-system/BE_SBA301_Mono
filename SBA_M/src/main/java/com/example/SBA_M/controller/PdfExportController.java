package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodDetailResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.tuition_search_response.AdmissionUniversityTuitionResponse;
import com.example.SBA_M.service.PdfExportService;
import com.example.SBA_M.service.UniversityAdmissionMethodService;
import com.example.SBA_M.service.UniversityMajorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/pdf-export")
@RequiredArgsConstructor
public class PdfExportController {

    private final PdfExportService pdfExportService;
    private final UniversityAdmissionMethodService admissionService;
    private final UniversityMajorService majorService;
    private static final String EXPORT_DIR = "exports/";

    @Operation(summary = "Export admission year groups to PDF")
    @GetMapping("/year-groups")
    public ApiResponse<String> exportYearGroups(
            @RequestParam Integer universityId
    ) {
        String fileName = generateFileName("year-groups", universityId);
        AdmissionUniversityTuitionResponse response = majorService.getAdmissionYearGroupsByUniversityId(universityId);
        pdfExportService.exportAdmissionYearGroupsToPdf(response, EXPORT_DIR + fileName);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Admission year groups PDF exported successfully")
                .result(fileName)
                .build();
    }

    @Operation(summary = "Export major admission details to PDF")
    @GetMapping("/major-admission")
    public ApiResponse<String> exportMajorAdmission(
            @RequestParam Integer universityId,
            @RequestParam Long majorId
    ) {
        String fileName = generateFileName("major-admission", universityId + "-" + majorId);
        MajorAdmissionResponse response = majorService.getMajorAdmissionByUniversityAndMajor(universityId, majorId);
        pdfExportService.exportMajorAdmissionToPdf(response, EXPORT_DIR + fileName);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Major admission PDF exported successfully")
                .result(fileName)
                .build();
    }

    @Operation(summary = "Export subject combination admission to PDF")
    @GetMapping("/subject-combination")
    public ApiResponse<String> exportSubjectCombination(
            @RequestParam Integer universityId,
            @RequestParam Long subjectCombinationId
    ) {
        String fileName = generateFileName("subject-combination", universityId + "-" + subjectCombinationId);
        SubjectCombinationResponse response = majorService.getSubjectCombinationAdmission(universityId, subjectCombinationId);
        pdfExportService.exportSubjectCombinationToPdf(response, EXPORT_DIR + fileName);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Subject combination PDF exported successfully")
                .result(fileName)
                .build();
    }

    @Operation(summary = "Export admission methods to PDF")
    @GetMapping("/admission-methods")
    public ApiResponse<String> exportAdmissionMethods(
            @RequestParam Integer universityId
    ) {
        String fileName = generateFileName("admission-methods", universityId);
        UniversityAdmissionMethodDetailResponse response = admissionService.getMethodsBySchool(universityId);
        pdfExportService.exportAdmissionMethodsToPdf(response, EXPORT_DIR + fileName);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Admission methods PDF exported successfully")
                .result(fileName)
                .build();
    }
    @Operation(summary = "Export admission year groups to PDF for download")
    @GetMapping("/year-groups/download")
    public void exportYearGroupsForDownload(
            @RequestParam Integer universityId,
            HttpServletResponse httpResponse
    ) throws IOException {
        AdmissionUniversityTuitionResponse response = majorService.getAdmissionYearGroupsByUniversityId(universityId);
        String fileName = generateFileName("admission-year-groups", universityId);
        pdfExportService.exportAdmissionYearGroupsToPdf(response, httpResponse);
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    @Operation(summary = "Export major admission details to PDF for download")
    @GetMapping("/major-admission/download")
    public void exportMajorAdmissionForDownload(
            @RequestParam Integer universityId,
            @RequestParam Long majorId,
            HttpServletResponse httpResponse
    ) throws IOException {
        MajorAdmissionResponse response = majorService.getMajorAdmissionByUniversityAndMajor(universityId, majorId);
        String fileName = generateFileName("major-admission", universityId + "-" + majorId);
        pdfExportService.exportMajorAdmissionToPdf(response, httpResponse);
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    @Operation(summary = "Export subject combination admission to PDF for download")
    @GetMapping("/subject-combination/download")
    public void exportSubjectCombinationForDownload(
            @RequestParam Integer universityId,
            @RequestParam Long subjectCombinationId,
            HttpServletResponse httpResponse
    ) throws IOException {
        SubjectCombinationResponse response = majorService.getSubjectCombinationAdmission(universityId, subjectCombinationId);
        String fileName = generateFileName("subject-combination", universityId + "-" + subjectCombinationId);
        pdfExportService.exportSubjectCombinationToPdf(response, httpResponse);
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    @Operation(summary = "Export admission methods to PDF for download")
    @GetMapping("/admission-methods/download")
    public void exportAdmissionMethodsForDownload(
            @RequestParam Integer universityId,
            HttpServletResponse httpResponse
    ) throws IOException {
        UniversityAdmissionMethodDetailResponse response = admissionService.getMethodsBySchool(universityId);
        String fileName = generateFileName("admission-methods", universityId);
        pdfExportService.exportAdmissionMethodsToPdf(response, httpResponse);
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }
    private String generateFileName(String prefix, Object identifier) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return prefix + "_" + identifier + "_" + timestamp + ".pdf";
    }
}