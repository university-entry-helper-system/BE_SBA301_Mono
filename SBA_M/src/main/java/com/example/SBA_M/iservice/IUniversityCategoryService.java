package com.example.SBA_M.iservice;

import com.example.SBA_M.entity.UniversityCategory;

import java.util.List;

public interface IUniversityCategoryService {
    List<UniversityCategory> getAllCategories();
    UniversityCategory saveCategory(UniversityCategory category);
}