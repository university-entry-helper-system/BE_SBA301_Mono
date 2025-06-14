package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.University;

import java.util.List;

public interface UniversityServiceImpl {
    List<University> getAllUniversities();
    University saveUniversity(University university);
}