package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.service.SubjectCombinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subject-combinations")
@RequiredArgsConstructor
public class SubjectCombinationController {

    private final SubjectCombinationService subjectCombinationService;

    @PostMapping
    public ResponseEntity<SubjectCombinationResponse> createSubjectCombination(
            @Valid @RequestBody SubjectCombinationRequest request) {
        SubjectCombinationResponse response = subjectCombinationService.createSubjectCombination(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectCombinationResponse> getSubjectCombinationById(
            @PathVariable Long id) {
        SubjectCombinationResponse response = subjectCombinationService.getSubjectCombinationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubjectCombinationResponse>> getAllSubjectCombinations() {
        List<SubjectCombinationResponse> responses = subjectCombinationService.getAllSubjectCombinations();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectCombinationResponse> updateSubjectCombination(
            @PathVariable Long id,
            @Valid @RequestBody SubjectCombinationRequest request) {
        SubjectCombinationResponse response = subjectCombinationService.updateSubjectCombination(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubjectCombination(@PathVariable Long id) {
        subjectCombinationService.deleteSubjectCombination(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Subject combination deleted successfully");
        return ResponseEntity.ok(response);
    }
}