package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityCategoryRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.service.UniversityCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/university-categories")
@RequiredArgsConstructor
public class UniversityCategoryController {

    private final UniversityCategoryService categoryService;

    @Operation(summary = "Get paginated categories", description = "Retrieve university categories with pagination")
    @GetMapping("/paginated")
    public ApiResponse<PageResponse<UniversityCategoryDocument>> getCategoriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UniversityCategoryDocument>>builder()
                .code(1000)
                .message("Categories fetched successfully")
                .result(categoryService.getCategoriesPaginated(page, size))
                .build();
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a university category by its ID")
    @GetMapping("/{id}")
    public ApiResponse<UniversityCategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ApiResponse.<UniversityCategoryResponse>builder()
                .code(1000)
                .message("Category fetched successfully")
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @Operation(
            summary = "Create a new category",
            description = "Create a new university category from the provided request data",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ApiResponse<UniversityCategoryResponse> createCategory(@Valid @RequestBody UniversityCategoryRequest categoryRequest) {
        return ApiResponse.<UniversityCategoryResponse>builder()
                .code(1001)
                .message("Category created successfully")
                .result(categoryService.createCategory(categoryRequest))
                .build();
    }

    @Operation(
            summary = "Update a category",
            description = "Update an existing university category with the provided request data",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ApiResponse<UniversityCategoryResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityCategoryRequest categoryRequest) {
        return ApiResponse.<UniversityCategoryResponse>builder()
                .code(1002)
                .message("Category updated successfully")
                .result(categoryService.updateCategory(id, categoryRequest))
                .build();
    }

    @Operation(
            summary = "Delete a category",
            description = "Delete a university category by its ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Category deleted successfully")
                .build();
    }
}
