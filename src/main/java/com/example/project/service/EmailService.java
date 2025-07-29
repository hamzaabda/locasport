package com.example.project.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String BASE_URL = "http://localhost:8080"; // À externaliser en production

    public void sendConfirmationEmail(String toEmail, String name, String token) {
        try {
            // Préparation du contexte
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("confirmationUrl", BASE_URL + "/api/v1/auth/confirm?token=" + token);

            // Traitement du template (notez le nom sans chemin)
            String htmlContent = templateEngine.process("confirmation", context);

            // Configuration de l'email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject("Confirmation de votre inscription");
            helper.setText(htmlContent, true); // true pour HTML
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Échec de l'envoi de l'email de confirmation", e);
        }
    }
}