package com.example.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.DTO.SatisfactionStats;
import com.example.project.model.FeedbackType;
import com.example.project.service.AnalysisService;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getStatsByType(@PathVariable String type) {
        try {
            FeedbackType feedbackType = FeedbackType.valueOf(type.toUpperCase());
            Map<String, SatisfactionStats> stats = analysisService.getSatisfactionStatsByType(feedbackType);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid feedback type: " + type);
        }
    }

    @GetMapping("/word-frequency")
    public ResponseEntity<?> getWordFrequency(@RequestParam(required = false) Integer minLength) {
        int length = minLength != null ? minLength : 4;
        if (length <= 0) {
            return ResponseEntity.badRequest().body("Minimum length must be positive");
        }
        
        Map<String, Long> frequency = analysisService.getWordFrequency(length);
        return ResponseEntity.ok(frequency);
    }
}