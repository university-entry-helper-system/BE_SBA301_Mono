package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ScoreRequest;
import com.example.SBA_M.dto.response.ScoreResponse;

import java.util.List;

public interface ScoreService {
    List<ScoreResponse> getAllScores();
    ScoreResponse getScoreById(Long id);
    ScoreResponse createScore(ScoreRequest request);
    ScoreResponse updateScore(Long id, ScoreRequest request);
    void deleteScore(Long id);
}