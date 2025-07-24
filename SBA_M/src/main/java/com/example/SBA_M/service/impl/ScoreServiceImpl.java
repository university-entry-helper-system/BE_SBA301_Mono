package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.ScoreRequest;
import com.example.SBA_M.dto.response.ScoreResponse;
import com.example.SBA_M.entity.commands.Score;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.ScoreMapper;
import com.example.SBA_M.repository.commands.ScoreRepository;
import com.example.SBA_M.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final ScoreMapper scoreMapper;

    @Override
    public List<ScoreResponse> getAllScores() {
        return scoreRepository.findAll().stream()
                .map(scoreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ScoreResponse getScoreById(Long id) {
        Score score = scoreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Score not found"));
        return scoreMapper.toResponse(score);
    }
    @Override
    public List<ScoreResponse> getAllScores(Integer year, String type) {
        List<Score> scores;
        if (year != null && type != null) {
            scores = scoreRepository.findByYearAndType(year, type);
        } else if (year != null) {
            scores = scoreRepository.findByYear(year);
        } else if (type != null) {
            scores = scoreRepository.findByType(type);
        } else {
            scores = scoreRepository.findAll();
        }
        return scores.stream().map(scoreMapper::toResponse).collect(Collectors.toList());
    }
    @Override
    public ScoreResponse createScore(ScoreRequest request) {
        Score score = scoreMapper.toEntity(request);
        return scoreMapper.toResponse(scoreRepository.save(score));
    }

    @Override
    public ScoreResponse updateScore(Long id, ScoreRequest request) {
        Score existing = scoreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Score not found"));
        existing.setSubject(request.getSubject());
        existing.setYear(request.getYear());
        existing.setScoreUrl(request.getScoreUrl());
        existing.setType(request.getType());
        return scoreMapper.toResponse(scoreRepository.save(existing));
    }

    @Override
    public void deleteScore(Long id) {
        if (!scoreRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND, "Score not found");
        }
        scoreRepository.deleteById(id);
    }
}