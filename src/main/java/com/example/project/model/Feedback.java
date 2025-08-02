package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String userName;
    private String email;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(length = 2000)
    private String comment;
    
    private LocalDateTime creationDate;
    
    @Enumerated(EnumType.STRING)
    private FeedbackType type;
    
    private String targetId;
    
    private boolean moderated;
}