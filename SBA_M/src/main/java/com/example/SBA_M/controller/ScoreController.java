package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ScoreRequest;
import com.example.SBA_M.dto.response.ScoreResponse;
import com.example.SBA_M.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/{id}")
    public ScoreResponse getScoreById(@PathVariable Long id) {
        return scoreService.getScoreById(id);
    }
    @GetMapping
    public List<ScoreResponse> getAllScores(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String type) {
        return scoreService.getAllScores(year, type);
    }
    @PostMapping
    public ScoreResponse createScore(@RequestBody ScoreRequest request) {
        return scoreService.createScore(request);
    }

    @PutMapping("/{id}")
    public ScoreResponse updateScore(@PathVariable Long id, @RequestBody ScoreRequest request) {
        return scoreService.updateScore(id, request);
    }
    @GetMapping("/filter")
    public List<ScoreResponse> getScoresByYearTypeAndSubject(
            @RequestParam Integer year,
            @RequestParam String type,
            @RequestParam String subject) {
        return scoreService.getScoresByYearTypeAndSubject(year, type, subject);
    }
    @DeleteMapping("/{id}")
    public void deleteScore(@PathVariable Long id) {
        scoreService.deleteScore(id);
    }
}