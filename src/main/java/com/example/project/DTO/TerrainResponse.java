package com.example.project.DTO;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerrainResponse {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String description;
    private double prixHeure;
    private boolean disponible;
    private String typeTerrain;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String createur;
}