package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityCategoryRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;


public interface UniversityCategoryService {

    /**
     * Get paginated categories
     */
    PageResponse<UniversityCategoryDocument> getCategoriesPaginated(int page, int size);

    /**
     * Get category by ID
     */
    UniversityCategoryResponse getCategoryById(Integer id);

    /**
     * Create a new university category
     */
    UniversityCategoryResponse createCategory(UniversityCategoryRequest request);

    /**
     * Update an existing university category
     */
    UniversityCategoryResponse updateCategory(Integer id, UniversityCategoryRequest request);

    /**
     * Delete a university category
     */
    void deleteCategory(Integer id);
}