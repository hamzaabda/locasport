package com.example.project.controller;

import com.example.project.model.Terrain;
import com.example.project.service.TerrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/terrains") // Changé pour correspondre à la config sécurité
@RequiredArgsConstructor
public class TerrainController {
    
    private final TerrainService terrainService;

    @PostMapping
    public ResponseEntity<Terrain> creerTerrain(@RequestBody Terrain terrain) {
        return ResponseEntity.ok(terrainService.creerTerrain(terrain));
    }

    @GetMapping
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainService.getAllTerrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Long id) {
        return terrainService.getTerrainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain terrainDetails) {
        return terrainService.updateTerrain(id, terrainDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable Long id) {
        terrainService.deleteTerrain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Terrain>> getTerrainsByType(@PathVariable Terrain.TypeTerrain type) {
        return ResponseEntity.ok(terrainService.getTerrainsByType(type));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Terrain>> getTerrainsDisponibles() {
        return ResponseEntity.ok(terrainService.getTerrainsDisponibles());
    }
}