package com.example.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Terrain {
    
    public enum TypeTerrain {
        FOOTBALL,
        BASKETBALL,
        TENNIS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String emplacement;
    
    @Column(nullable = false)
    private double prixHeure;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTerrain type;
    
    @Column(nullable = false)
    private boolean estDisponible = true;
}