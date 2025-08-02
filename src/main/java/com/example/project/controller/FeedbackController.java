package com.example.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.DTO.FeedbackRequest;
import com.example.project.DTO.FeedbackResponse;
import com.example.project.DTO.SatisfactionStats;
import com.example.project.service.FeedbackService;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        // Validation manuelle complète
        if (feedbackRequest == null) {
            return ResponseEntity.badRequest().body("Feedback request cannot be null");
        }
        
        if (feedbackRequest.getTargetId() == null || feedbackRequest.getTargetId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Target ID is required");
        }
        
        if (feedbackRequest.getRating() == null || feedbackRequest.getRating() < 1 || feedbackRequest.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }
        
        if (feedbackRequest.getType() == null) {
            return ResponseEntity.badRequest().body("Feedback type is required");
        }
        
        try {
            FeedbackResponse response = feedbackService.createFeedback(feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error creating feedback: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<?> getFeedbackByTarget(@PathVariable String targetId) {
        if (targetId == null || targetId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Target ID is required");
        }
        
        List<FeedbackResponse> feedbacks = feedbackService.getFeedbackByTarget(targetId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/stats/{targetId}")
    public ResponseEntity<?> getSatisfactionStats(@PathVariable String targetId) {
        if (targetId == null || targetId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Target ID is required");
        }
        
        SatisfactionStats stats = feedbackService.getSatisfactionStats(targetId);
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/moderate/{feedbackId}")
    public ResponseEntity<?> moderateFeedback(
            @PathVariable Long feedbackId,
            @RequestParam boolean approved) {
        if (feedbackId == null || feedbackId <= 0) {
            return ResponseEntity.badRequest().body("Invalid feedback ID");
        }
        
        try {
            feedbackService.moderateFeedback(feedbackId, approved);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error moderating feedback: " + e.getMessage());
        }
    }
}