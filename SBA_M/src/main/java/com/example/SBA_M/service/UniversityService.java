package com.example.SBA_M.service;


import com.example.SBA_M.entity.University;
import com.example.SBA_M.iservice.IUniversityService;
import com.example.SBA_M.repository.UniversityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService implements IUniversityService {
    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Override
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    @Override
    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }
}