package com.example.SBA_M.service;

import com.example.SBA_M.entity.University;

import java.util.List;

public interface UniversityService {
    List<University> getAllUniversities();
    University saveUniversity(University university);
}