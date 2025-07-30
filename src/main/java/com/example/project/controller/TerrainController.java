package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.Terrain;
import com.example.project.service.TerrainService;

import java.util.List;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {
    private final TerrainService terrainService;

    @Autowired
    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @PostMapping
    public Terrain creerTerrain(@RequestBody Terrain terrain) {
        return terrainService.creerTerrain(terrain);
    }

    @GetMapping
    public List<Terrain> getAllTerrains() {
        return terrainService.getAllTerrains();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Long id) {
        Terrain terrain = terrainService.getTerrainById(id);
        if (terrain != null) {
            return ResponseEntity.ok(terrain);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain terrainDetails) {
        Terrain updatedTerrain = terrainService.updateTerrain(id, terrainDetails);
        if (updatedTerrain != null) {
            return ResponseEntity.ok(updatedTerrain);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable Long id) {
        terrainService.deleteTerrain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public List<Terrain> getTerrainsByType(@PathVariable Terrain.TypeTerrain type) {
        return terrainService.getTerrainsByType(type);
    }

    @GetMapping("/disponibles")
    public List<Terrain> getTerrainsDisponibles() {
        return terrainService.getTerrainsDisponibles();
    }
}
