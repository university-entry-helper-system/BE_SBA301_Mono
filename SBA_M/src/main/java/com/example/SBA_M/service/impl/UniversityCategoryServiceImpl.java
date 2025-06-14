package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.UniversityCategory;

import java.util.List;

public interface UniversityCategoryServiceImpl {
    List<UniversityCategory> getAllCategories();
    UniversityCategory saveCategory(UniversityCategory category);
}