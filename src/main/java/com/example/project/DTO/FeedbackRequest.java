package com.example.project.DTO;

import com.example.project.model.FeedbackType;
import lombok.Data;

@Data
public class FeedbackRequest {
    private String userId;
    private String userName;
    private String email;
    private Integer rating;
    private String comment;
    private FeedbackType type;
    private String targetId;
}