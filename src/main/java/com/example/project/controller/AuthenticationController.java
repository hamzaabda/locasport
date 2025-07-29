package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.repository.UserRepository;
import com.example.project.service.AuthenticationService;
import com.example.project.service.ConfirmationTokenService;
import com.example.project.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Endpoint REST pour l'inscription
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    // Endpoint HTML pour la confirmation d'email
    @GetMapping("/confirm")
    public ModelAndView confirmEmail(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView();
        
        try {
            ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
            
            if (confirmationToken.getConfirmedAt() != null) {
                modelAndView.addObject("error", "Email déjà confirmé");
                modelAndView.setViewName("confirmation-result");
                return modelAndView;
            }
            
            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                modelAndView.addObject("error", "Lien expiré");
                modelAndView.setViewName("confirmation-result");
                return modelAndView;
            }
            
            confirmationTokenService.setConfirmedAt(token);
            User user = confirmationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            
            modelAndView.addObject("success", true);
            modelAndView.addObject("message", "Votre email a été confirmé avec succès !");
            modelAndView.setViewName("confirmation-result");
            
        } catch (Exception e) {
            modelAndView.addObject("error", "Lien de confirmation invalide");
            modelAndView.setViewName("confirmation-result");
        }
        
        return modelAndView;
    }

    // Endpoint REST pour le login
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    // Endpoint pour renvoyer un email de confirmation
    @PostMapping("/resend-confirmation")
    @ResponseBody
    public ResponseEntity<String> resendConfirmationEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Email non trouvé"));
        
        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body("Email déjà confirmé");
        }
        
        ConfirmationToken newToken = confirmationTokenService.createConfirmationToken(user);
        emailService.sendConfirmationEmail(user.getEmail(), user.getFirstname(), newToken.getToken());
        
        return ResponseEntity.ok("Email de confirmation renvoyé");
    }
}