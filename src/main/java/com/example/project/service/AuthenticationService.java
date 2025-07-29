package com.example.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.model.AuthenticationRequest;
import com.example.project.model.AuthenticationResponse;
import com.example.project.model.ConfirmationToken;
import com.example.project.model.RegisterRequest;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false) // User is disabled until email confirmation
                .build();
        
        var savedUser = repository.save(user);
        var jwtToken = generateTokenWithRole(user);
        
        // Create and save confirmation token
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(savedUser);
        
        // Send confirmation email
        emailService.sendConfirmationEmail(
            user.getEmail(),
            user.getFirstname(),
            confirmationToken.getToken()
        );
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        
        if (!user.isEnabled()) {
            throw new IllegalStateException("Account not activated. Please confirm your email first.");
        }
        
        var jwtToken = generateTokenWithRole(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .enabled(true) // Admin accounts are enabled immediately
                .build();
        repository.save(user);
        var jwtToken = generateTokenWithRole(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String generateTokenWithRole(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        return jwtService.generateToken(extraClaims, user);
    }
}