package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityCategoryRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.service.UniversityCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/university-categories")
@SecurityRequirement(name = "bearerAuth")
public class UniversityCategoryController {
    private final UniversityCategoryService categoryService;

    public UniversityCategoryController(UniversityCategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "Get paginated categories", description = "Retrieve university categories with pagination")
    @GetMapping("/paginated")
    public ResponseEntity<PageResponse<UniversityCategoryDocument>> getCategoriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(categoryService.getCategoriesPaginated(page, size));
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a university category by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<UniversityCategoryResponse> getCategoryById(@PathVariable Integer id) {
        UniversityCategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Create a new category", description = "Create a new university category from the provided request data")
    @PostMapping
    public ResponseEntity<UniversityCategoryResponse> createCategory(@Valid @RequestBody UniversityCategoryRequest categoryRequest) {
        UniversityCategoryResponse created = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a category", description = "Update an existing university category with the provided request data")
    @PutMapping("/{id}")
    public ResponseEntity<UniversityCategoryResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityCategoryRequest categoryRequest) {
        UniversityCategoryResponse updated = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a category", description = "Delete a university category by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("University category deleted successfully");
    }
}