package com.example.project.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.example.project.model.RegisterRequest;

import com.example.project.repository.UserRepository;
import com.example.project.service.AuthenticationService;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final AuthenticationService authService;
    private final UserRepository userRepository; // Ajoutez ce repository

    @PostConstruct
    public void init() {
        // Vérifier si l'admin existe déjà avant de le créer
        String adminEmail = "admin@example.com";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            try {
                authService.registerAdmin(RegisterRequest.builder()
                        .firstname("Admin")
                        .lastname("System")
                        .email(adminEmail)
                        .password("admin123")
                        .build());
                System.out.println("Admin user created successfully");
            } catch (Exception e) {
                System.err.println("Error creating admin user: " + e.getMessage());
            }
        } else {
            System.out.println("Admin user already exists, skipping creation");
        }
    }
}