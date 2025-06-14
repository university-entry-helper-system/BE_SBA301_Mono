package com.example.SBA_M.service;

import com.example.SBA_M.entity.UniversityCategory;
import com.example.SBA_M.repository.UniversityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityCategoryServiceImpl implements com.example.SBA_M.service.impl.UniversityCategoryServiceImpl {
    private final UniversityCategoryRepository categoryRepository;

    public UniversityCategoryServiceImpl(UniversityCategoryRepository categoryRepository) {
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