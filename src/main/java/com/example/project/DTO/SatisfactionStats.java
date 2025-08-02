package com.example.project.DTO;

import lombok.Data;

@Data
public class SatisfactionStats {
    private double averageRating;
    private int totalFeedback;
    private int rating1Count;
    private int rating2Count;
    private int rating3Count;
    private int rating4Count;
    private int rating5Count;
    private double positivePercentage;
}
