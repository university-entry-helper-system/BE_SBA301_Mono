package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.University;

import java.util.List;

public interface IUniversityService {
    List<University> getAllUniversities();
    University saveUniversity(University university);
}