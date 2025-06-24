package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;

public interface UniversityService {
    /**
     * Get all universities with pagination
     */
    PageResponse<University> getAllUniversities(int page, int size);

    /**
     * Save a university entity
     */
    University saveUniversity(University university);

    /**
     * Get university document by ID
     */
    UniversityDocument getUniversityById(Integer id);

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
}