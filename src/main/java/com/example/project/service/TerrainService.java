package com.example.project.service;

import com.example.project.model.Terrain;
import com.example.project.repository.TerrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerrainService {
    
    private final TerrainRepository terrainRepository;

    public Terrain creerTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    public Optional<Terrain> getTerrainById(Long id) {
        return terrainRepository.findById(id);
    }

    public Optional<Terrain> updateTerrain(Long id, Terrain terrainDetails) {
        return terrainRepository.findById(id)
                .map(terrain -> {
                    terrain.setNom(terrainDetails.getNom());
                    terrain.setEmplacement(terrainDetails.getEmplacement());
                    terrain.setPrixHeure(terrainDetails.getPrixHeure());
                    terrain.setType(terrainDetails.getType());
                    terrain.setEstDisponible(terrainDetails.isEstDisponible());
                    return terrainRepository.save(terrain);
                });
    }

    public void deleteTerrain(Long id) {
        terrainRepository.deleteById(id);
    }

    public List<Terrain> getTerrainsByType(Terrain.TypeTerrain type) {
        return terrainRepository.findByType(type);
    }

    public List<Terrain> getTerrainsDisponibles() {
        return terrainRepository.findByEstDisponible(true);
    }
}