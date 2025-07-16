package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.CampusTypeRequest;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface CampusTypeService {
    /**
     * Get all campus types
     */
    List<CampusTypeResponse> getAllCampusTypes();

    /**
     * Get campus type by ID
     */
    CampusTypeResponse getCampusTypeById(Integer id);

    /**
     * Create a new campus type
     */
    CampusTypeResponse createCampusType(CampusTypeRequest request);

    /**
     * Update an existing campus type
     */
    CampusTypeResponse updateCampusType(Integer id, CampusTypeRequest request);

    /**
     * Delete campus type by ID
     */
    void deleteCampusType(Integer id);

    /**
     * Update campus type status
     */
    CampusTypeResponse updateCampusTypeStatus(Integer id, Status status);

    /**
     * Search, paginate, and sort campus types
     */
    Object searchCampusTypes(String search, int page, int size, String sort);
} 