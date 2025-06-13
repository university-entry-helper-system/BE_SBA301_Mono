package com.example.SBA_M.service;

import com.example.SBA_M.entity.UniversityCategory;
import com.example.SBA_M.iservice.IUniversityCategoryService;
import com.example.SBA_M.repository.UniversityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityCategoryService implements IUniversityCategoryService {
    private final UniversityCategoryRepository categoryRepository;

    public UniversityCategoryService(UniversityCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<UniversityCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public UniversityCategory saveCategory(UniversityCategory category) {
        return categoryRepository.save(category);
    }
}