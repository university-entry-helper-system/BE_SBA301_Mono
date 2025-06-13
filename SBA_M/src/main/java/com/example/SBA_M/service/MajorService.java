package com.example.SBA_M.service;
import com.example.SBA_M.entity.Major;
import com.example.SBA_M.iservice.IMajorService;
import com.example.SBA_M.repository.MajorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService implements IMajorService {
    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    @Override
    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    @Override
    public Major saveMajor(Major major) {
        return majorRepository.save(major);
    }
}