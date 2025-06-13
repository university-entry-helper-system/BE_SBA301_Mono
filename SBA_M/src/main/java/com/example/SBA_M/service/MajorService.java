package com.example.SBA_M.service;
import com.example.SBA_M.entity.Major;
import com.example.SBA_M.repository.MajorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {
    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public Major saveMajor(Major major) {
        return majorRepository.save(major);
    }
}