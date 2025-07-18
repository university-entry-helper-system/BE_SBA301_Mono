package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.UniversityAdmissionMethodDetailResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.tuition_search_response.AdmissionUniversityTuitionResponse;

public interface PdfExportService {
    void exportAdmissionYearGroupsToPdf(AdmissionUniversityTuitionResponse response, String outputPath);
    void exportMajorAdmissionToPdf(MajorAdmissionResponse response, String outputPath);
    void exportSubjectCombinationToPdf(SubjectCombinationResponse response, String outputPath);
    void exportAdmissionMethodsToPdf(UniversityAdmissionMethodDetailResponse response, String outputPath);
}