package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.Major;

import java.util.List;

public interface IMajorService {
    List<Major> getAllMajors();
    Major saveMajor(Major major);
}