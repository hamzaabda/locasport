package com.example.project.service;


import org.springframework.stereotype.Service;

import com.example.project.DTO.SatisfactionStats;
import com.example.project.model.Feedback;
import com.example.project.model.FeedbackType;
import com.example.project.repository.FeedbackRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final FeedbackRepository feedbackRepository;

    public AnalysisService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Map<String, SatisfactionStats> getSatisfactionStatsByType(FeedbackType type) {
        List<Feedback> feedbacks = feedbackRepository.findByType(type);
        
        Map<String, List<Feedback>> feedbackByTarget = feedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getTargetId));
        
        Map<String, SatisfactionStats> statsMap = new HashMap<>();
        
        feedbackByTarget.forEach((targetId, targetFeedbacks) -> {
            SatisfactionStats stats = new SatisfactionStats();
            
            double average = targetFeedbacks.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);
            
            int total = targetFeedbacks.size();
            int rating1 = (int) targetFeedbacks.stream().filter(f -> f.getRating() == 1).count();
            int rating2 = (int) targetFeedbacks.stream().filter(f -> f.getRating() == 2).count();
            int rating3 = (int) targetFeedbacks.stream().filter(f -> f.getRating() == 3).count();
            int rating4 = (int) targetFeedbacks.stream().filter(f -> f.getRating() == 4).count();
            int rating5 = (int) targetFeedbacks.stream().filter(f -> f.getRating() == 5).count();
            
            double positivePercentage = (rating4 + rating5) * 100.0 / total;
            
            stats.setAverageRating(average);
            stats.setTotalFeedback(total);
            stats.setRating1Count(rating1);
            stats.setRating2Count(rating2);
            stats.setRating3Count(rating3);
            stats.setRating4Count(rating4);
            stats.setRating5Count(rating5);
            stats.setPositivePercentage(positivePercentage);
            
            statsMap.put(targetId, stats);
        });
        
        return statsMap;
    }
    
    public Map<String, Long> getWordFrequency(int minWordLength) {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        
        return feedbacks.stream()
                .filter(f -> f.getComment() != null && !f.getComment().isEmpty())
                .flatMap(f -> List.of(f.getComment().toLowerCase().split("\\s+")).stream())
                .filter(word -> word.length() >= minWordLength)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(50)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        HashMap::new
                ));
    }
}