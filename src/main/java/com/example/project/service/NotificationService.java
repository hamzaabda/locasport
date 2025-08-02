package com.example.project.service;

import com.example.project.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    // Pour les notifications email
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    // Pour les notifications WebSocket
    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;
    
    // Pour les notifications SSE (Server-Sent Events)
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    // Configuration
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String WS_DESTINATION = "/topic/notifications";
    
    public void notifyNewFeedback(Feedback feedback) {
        // 1. Log dans la console (toujours actif)
        System.out.println("[Notification] Nouveau feedback #" + feedback.getId() + 
                         " pour " + feedback.getTargetId() + 
                         " - Note: " + feedback.getRating());
        
        // 2. Notification par email (si configuré)
        sendEmailNotification(feedback);
        
        // 3. Notification WebSocket (si configuré)
        sendWebSocketNotification(feedback);
        
        // 4. Notification SSE (Server-Sent Events)
        sendSseNotification(feedback);
    }
    
    private void sendEmailNotification(Feedback feedback) {
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(ADMIN_EMAIL);
                message.setSubject("Nouveau feedback reçu");
                message.setText(String.format(
                    "Un nouveau feedback a été soumis:\n\n" +
                    "ID: %d\n" +
                    "Cible: %s\n" +
                    "Note: %d/5\n" +
                    "Commentaire: %s\n\n" +
                    "Date: %s",
                    feedback.getId(),
                    feedback.getTargetId(),
                    feedback.getRating(),
                    feedback.getComment() != null ? feedback.getComment() : "Aucun",
                    feedback.getCreationDate()
                ));
                
                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            }
        }
    }
    
    private void sendWebSocketNotification(Feedback feedback) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend(WS_DESTINATION, 
                new NotificationPayload(
                    "NEW_FEEDBACK",
                    "Nouveau feedback pour " + feedback.getTargetId(),
                    feedback
                )
            );
        }
    }
    
    private void sendSseNotification(Feedback feedback) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("new-feedback")
                    .data(feedback));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
    
    // Méthode pour l'abonnement SSE
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(3600000L); // 1 heure timeout
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        
        return emitter;
    }
    
    // Classe interne pour le payload WebSocket
    private static class NotificationPayload {
        private String type;
        private String message;
        private Feedback feedback;
        
        public NotificationPayload(String type, String message, Feedback feedback) {
            this.type = type;
            this.message = message;
            this.feedback = feedback;
        }
        
        // Getters (nécessaires pour la sérialisation JSON)
        public String getType() { return type; }
        public String getMessage() { return message; }
        public Feedback getFeedback() { return feedback; }
    }
}