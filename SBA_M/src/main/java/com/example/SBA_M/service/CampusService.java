package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.CampusRequest;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface CampusService {
    /**
     * Get all campuses with search, pagination, sort and filters
     */
    PageResponse<CampusResponse> getAllCampuses(String search, int page, int size, String sort, 
                                               Integer universityId, Integer provinceId, 
                                               String campusType, Boolean isMainCampus);

    /**
     * Get campus by ID
     */
    CampusResponse getCampusById(Integer id);

    /**
     * Create a new campus
     */
    CampusResponse createCampus(CampusRequest request);

    /**
     * Update an existing campus
     */
    CampusResponse updateCampus(Integer id, CampusRequest request);

    /**
     * Delete campus by ID
     */
    void deleteCampus(Integer id);

    /**
     * Get campuses by university ID
     */
    List<CampusResponse> getCampusesByUniversity(Integer universityId, Boolean includeInactive);

    /**
     * Get campuses by province ID
     */
    PageResponse<CampusResponse> getCampusesByProvince(Integer provinceId, int page, int size, String sort);

    /**
     * Update campus status
     */
    CampusResponse updateCampusStatus(Integer id, Status status);
} 