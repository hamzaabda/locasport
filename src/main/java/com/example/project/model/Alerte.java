package com.example.project.model;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "alertes")
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type; // METEO, FERMETURE, etc.
    
    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation;
    
    @Column(nullable = false)
    private LocalDateTime dateExpiration;
    
    @Column(nullable = false)
    private String niveauUrgence; // INFO, URGENT, CRITIQUE
    
    @Column(nullable = false)
    private boolean active = true;
}