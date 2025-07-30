package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.Terrain;
import com.example.project.service.TerrainService;

import java.util.List;

@RestController
@RequestMapping("/api/display/terrains")
public class TerrainDisplayController {
    private final TerrainService terrainService;

    @Autowired
    public TerrainDisplayController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @GetMapping
    public List<Terrain> afficherTousLesTerrains() {
        return terrainService.getAllTerrains();
    }

    @GetMapping("/{id}")
    public Terrain afficherTerrainParId(@PathVariable Long id) {
        return terrainService.getTerrainById(id);
    }

    @GetMapping("/type/{type}")
    public List<Terrain> afficherTerrainsParType(@PathVariable Terrain.TypeTerrain type) {
        return terrainService.getTerrainsByType(type);
    }

    @GetMapping("/disponibles")
    public List<Terrain> afficherTerrainsDisponibles() {
        return terrainService.getTerrainsDisponibles();
    }
}