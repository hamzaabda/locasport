package com.example.project.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerrainRequest {
    private String nom;
    private String adresse;
    private String ville;
    private String description;
    private double prixHeure;
    private boolean disponible;
    private String typeTerrain;
}