package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.UniversityService;
import com.example.SBA_M.service.messaging.producer.UniversityProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final UniversityProducer universityProducer;
    private final UniversityReadRepository universityReadRepository;


    @Override
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    @Override
    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    @Override
    public UniversityDocument getUniversityById(Integer id) {
        return universityReadRepository.findById(id).orElseThrow(() -> new RuntimeException("University not found with id: " + id));
    }

    @Override
    public University createUniversity(UniversityRequest university) {
        return universityProducer.createUniversity(university);
    }
}