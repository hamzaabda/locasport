package com.example.project.service;

import com.example.project.model.ConfirmationToken;
import com.example.project.model.User;
import com.example.project.repository.ConfirmationTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    
    public ConfirmationToken createConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(15); // Token valid for 15 minutes
        
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token, createdAt, expiresAt, user
        );
        
        return confirmationTokenRepository.save(confirmationToken);
    }
    
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
    }
    
    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
            token, LocalDateTime.now()
        );
    }
}