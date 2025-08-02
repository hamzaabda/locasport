package com.example.project.service;

import com.example.project.DTO.FeedbackRequest;
import com.example.project.DTO.FeedbackResponse;
import com.example.project.DTO.SatisfactionStats;
import com.example.project.model.Feedback;
import com.example.project.model.FeedbackType;
import com.example.project.repository.FeedbackRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    @PostConstruct
    public void configureModelMapper() {
        // Configuration pour éviter les conflits de mapping
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        
        modelMapper.typeMap(FeedbackRequest.class, Feedback.class)
            .addMappings(mapper -> {
                mapper.skip(Feedback::setId);
                mapper.skip(Feedback::setCreationDate);
                mapper.skip(Feedback::setModerated);
            });
    }

    public FeedbackResponse createFeedback(FeedbackRequest feedbackRequest) {
        // Validation manuelle
        if (feedbackRequest.getTargetId() == null || feedbackRequest.getTargetId().isBlank()) {
            throw new IllegalArgumentException("Target ID is required");
        }
        
        if (feedbackRequest.getRating() == null || feedbackRequest.getRating() < 1 || feedbackRequest.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Mapping avec ModelMapper configuré
        Feedback feedback = modelMapper.map(feedbackRequest, Feedback.class);
        feedback.setCreationDate(LocalDateTime.now());
        feedback.setModerated(false);
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        notificationService.notifyNewFeedback(savedFeedback);
        
        return modelMapper.map(savedFeedback, FeedbackResponse.class);
    }

    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll().stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getFeedbackByTarget(String targetId) {
        if (targetId == null || targetId.isBlank()) {
            throw new IllegalArgumentException("Target ID is required");
        }
        
        return feedbackRepository.findByTargetId(targetId).stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());
    }

    public SatisfactionStats getSatisfactionStats(String targetId) {
        if (targetId == null || targetId.isBlank()) {
            throw new IllegalArgumentException("Target ID is required");
        }

        Double averageRating = feedbackRepository.findAverageRatingByTargetId(targetId);
        Integer totalFeedback = feedbackRepository.countByTargetId(targetId);
        
        if (averageRating == null || totalFeedback == 0) {
            return new SatisfactionStats();
        }
        
        List<Feedback> feedbacks = feedbackRepository.findByTargetId(targetId);
        
        int rating1 = (int) feedbacks.stream().filter(f -> f.getRating() == 1).count();
        int rating2 = (int) feedbacks.stream().filter(f -> f.getRating() == 2).count();
        int rating3 = (int) feedbacks.stream().filter(f -> f.getRating() == 3).count();
        int rating4 = (int) feedbacks.stream().filter(f -> f.getRating() == 4).count();
        int rating5 = (int) feedbacks.stream().filter(f -> f.getRating() == 5).count();
        
        double positivePercentage = (rating4 + rating5) * 100.0 / totalFeedback;
        
        SatisfactionStats stats = new SatisfactionStats();
        stats.setAverageRating(averageRating);
        stats.setTotalFeedback(totalFeedback);
        stats.setRating1Count(rating1);
        stats.setRating2Count(rating2);
        stats.setRating3Count(rating3);
        stats.setRating4Count(rating4);
        stats.setRating5Count(rating5);
        stats.setPositivePercentage(positivePercentage);
        
        return stats;
    }
    
    @Transactional
    public void moderateFeedback(Long feedbackId, boolean approved) {
        if (feedbackId == null || feedbackId <= 0) {
            throw new IllegalArgumentException("Invalid feedback ID");
        }
        
        feedbackRepository.findById(feedbackId).ifPresent(feedback -> {
            feedback.setModerated(true);
            if (!approved) {
                feedbackRepository.delete(feedback);
            } else {
                feedbackRepository.save(feedback);
            }
        });
    }
}