package com.example.SBA_M.controller;

import com.example.SBA_M.entity.commands.UniversityCategory;
import com.example.SBA_M.service.UniversityCategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/university-categories")
@SecurityRequirement(name = "bearerAuth")
public class UniversityCategoryController {
    private final UniversityCategoryService categoryService;

    public UniversityCategoryController(UniversityCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<UniversityCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public UniversityCategory createCategory(@RequestBody UniversityCategory category) {
        return categoryService.saveCategory(category);
    }
}