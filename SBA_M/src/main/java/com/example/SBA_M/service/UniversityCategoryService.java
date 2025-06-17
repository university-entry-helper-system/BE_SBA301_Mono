package com.example.SBA_M.service;

import com.example.SBA_M.entity.commands.UniversityCategory;

import java.util.List;

public interface UniversityCategoryService {
    List<UniversityCategory> getAllCategories();
    UniversityCategory saveCategory(UniversityCategory category);
}