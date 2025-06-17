package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.repository.commands.MajorRepository;
import com.example.SBA_M.service.MajorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;

    public MajorServiceImpl(MajorRepository majorRepository) {
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