package com.example.project.DTO;

import com.example.project.model.FeedbackType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private Long id;
    private String userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime creationDate;
    private FeedbackType type;
    private String targetId;
}