package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.GraduationScoreRequest;
import com.example.SBA_M.dto.response.GraduationScoreResponse;

public interface GraduationScoreService {
     GraduationScoreResponse calculateGraduationScore(GraduationScoreRequest request);
    }
