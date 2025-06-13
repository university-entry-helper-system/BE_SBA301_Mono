package com.example.SBA_M.iservice;

import com.example.SBA_M.entity.University;

import java.util.List;

public interface IUniversityService {
    List<University> getAllUniversities();
    University saveUniversity(University university);
}