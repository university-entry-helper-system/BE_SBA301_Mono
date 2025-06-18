package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;

import java.util.List;

public interface UniversityService {
    List<University> getAllUniversities();
    University saveUniversity(University university);
    UniversityDocument getUniversityById(Integer id);
    University createUniversity(UniversityRequest university);
}