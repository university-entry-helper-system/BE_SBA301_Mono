package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.utils.Status;

public interface UniversityService {
    /**
     * Get all universities with search, pagination, sort and filters
     */
    PageResponse<UniversityResponse> getAllUniversities(String search, int page, int size, String sort, Integer categoryId, Integer provinceId, Boolean includeCampuses);

    /**
     * Save a university entity
     */
    University saveUniversity(University university);

    /**
     * Get university document by ID
     */
    UniversityResponse getUniversityById(Integer id);

    /**
     * Create a new university from request
     */
    UniversityResponse createUniversity(UniversityRequest university);

    /**
     * Update an existing university
     */
    UniversityResponse updateUniversity(Integer id, UniversityRequest universityRequest);

    /**
     * Delete university by ID
     */
    void deleteUniversity(Integer id);

    /**
     * Update university status
     */
    UniversityResponse updateUniversityStatus(Integer id, Status status);

    /**
     * Get university by code
     */
    UniversityResponse getUniversityByCode(String universityCode);

    /**
     * Get university by name
     */
    UniversityResponse getUniversityByName(String name);

    /**
     * Get university by short name
     */
    UniversityResponse getUniversityByShortName(String shortName);

    /**
     * Get universities by province
     */
    PageResponse<UniversityResponse> getUniversitiesByProvince(Integer provinceId, Boolean includeMainCampusOnly, int page, int size, String sort);
}