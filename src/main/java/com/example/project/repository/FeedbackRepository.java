package com.example.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project.model.Feedback;
import com.example.project.model.FeedbackType;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTargetId(String targetId);
    List<Feedback> findByType(FeedbackType type);
    List<Feedback> findByRatingBetween(int min, int max);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.targetId = ?1")
    Double findAverageRatingByTargetId(String targetId);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.targetId = ?1")
    Integer countByTargetId(String targetId);
}
