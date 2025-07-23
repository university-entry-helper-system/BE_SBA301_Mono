package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityMajorRequest;
import com.example.SBA_M.dto.response.*;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.tuition_search_response.AdmissionUniversityTuitionResponse;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;


public interface UniversityMajorService {
    PageResponse<UniversityMajorResponse> getAllUniversityMajors(Integer universityId,String name, int page, int size);

    UniversityMajorResponse getUniversityMajorById(Integer id);

    UniversityMajorResponse createUniversityMajor(UniversityMajorRequest request);

    UniversityMajorResponse updateUniversityMajor(Integer id, UniversityMajorRequest request);

    void deleteUniversityMajor(Integer id);

    AdmissionUniversityTuitionResponse getAdmissionYearGroupsByUniversityId(Integer universityId);

    MajorAdmissionResponse getMajorAdmissionByUniversityAndMajor(Integer universityId, Long majorId);

    SubjectCombinationResponse getSubjectCombinationAdmission(Integer universityId, Long subjectCombinationId);

    List<UniversitySubjectCombinationSearchResponse> searchBySubjectCombination (
            Long subjectCombinationId, @Nullable String universityName) throws IOException;
    List<UniversityMajorResponse> getAllUniversityMajors();
    List<UniversityMajorAdmissionResponse> findEligibleMajors(Double score, Long subjectCombinationId, Integer maxGap);

    List<UniversityMajorSearchResponse> searchByMajor (
            Long majorId, @Nullable String universityName) throws IOException;
}
