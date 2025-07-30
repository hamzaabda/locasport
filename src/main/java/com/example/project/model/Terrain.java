package com.example.project.model;



import jakarta.persistence.*;

@Entity
public class Terrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String emplacement;
    private double prixHeure;
    
    @Enumerated(EnumType.STRING)
    private TypeTerrain type;
    
    private boolean estDisponible;

    // Enum pour les types de terrain
    public enum TypeTerrain {
        FOOTBALL,
        BASKETBALL,
        TENNIS
    }

    // Constructeurs
    public Terrain() {
    }

    public Terrain(String nom, String emplacement, double prixHeure, TypeTerrain type, boolean estDisponible) {
        this.nom = nom;
        this.emplacement = emplacement;
        this.prixHeure = prixHeure;
        this.type = type;
        this.estDisponible = estDisponible;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public double getPrixHeure() {
        return prixHeure;
    }

    public void setPrixHeure(double prixHeure) {
        this.prixHeure = prixHeure;
    }

    public TypeTerrain getType() {
        return type;
    }

    public void setType(TypeTerrain type) {
        this.type = type;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }
}