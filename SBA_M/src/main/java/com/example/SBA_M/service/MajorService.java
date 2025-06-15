package com.example.SBA_M.service;

import com.example.SBA_M.entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> getAllMajors();
    Major saveMajor(Major major);
}