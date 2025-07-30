package com.example.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.Terrain;
import com.example.project.repository.TerrainRepository;

@Service
public class TerrainService {
    private final TerrainRepository terrainRepository;

    @Autowired
    public TerrainService(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    // Créer un nouveau terrain
    public Terrain creerTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    // Récupérer tous les terrains
    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    // Récupérer un terrain par son ID
    public Terrain getTerrainById(Long id) {
        return terrainRepository.findById(id).orElse(null);
    }

    // Mettre à jour un terrain
    public Terrain updateTerrain(Long id, Terrain terrainDetails) {
        Terrain terrain = terrainRepository.findById(id).orElse(null);
        if (terrain != null) {
            terrain.setNom(terrainDetails.getNom());
            terrain.setEmplacement(terrainDetails.getEmplacement());
            terrain.setPrixHeure(terrainDetails.getPrixHeure());
            terrain.setType(terrainDetails.getType());
            terrain.setEstDisponible(terrainDetails.isEstDisponible());
            return terrainRepository.save(terrain);
        }
        return null;
    }

    // Supprimer un terrain
    public void deleteTerrain(Long id) {
        terrainRepository.deleteById(id);
    }

    // Récupérer les terrains par type
    public List<Terrain> getTerrainsByType(Terrain.TypeTerrain type) {
        return terrainRepository.findByType(type);
    }

    // Récupérer les terrains disponibles
    public List<Terrain> getTerrainsDisponibles() {
        return terrainRepository.findByEstDisponible(true);
    }
}
