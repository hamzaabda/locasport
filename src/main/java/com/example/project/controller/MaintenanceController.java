package com.example.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.example.project.model.Maintenance;
import com.example.project.repository.TerrainRepository;
import com.example.project.service.MaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;
     private final TerrainRepository terrainRepository;
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleMaintenance(@RequestBody Maintenance maintenance) {
        // Vérifier si le terrain existe
        if (maintenance.getTerrain() == null || maintenance.getTerrain().getId() == null) {
            return ResponseEntity.badRequest().body("Terrain ID is required");
        }
        
        if (!terrainRepository.existsById(maintenance.getTerrain().getId())) {
            return ResponseEntity.badRequest().body("Terrain not found with ID: " + maintenance.getTerrain().getId());
        }
        
        return ResponseEntity.ok(maintenanceService.scheduleMaintenance(maintenance));
    }
    
    @GetMapping("/terrain/{terrainId}")
    public ResponseEntity<List<Maintenance>> getTerrainMaintenance(@PathVariable Long terrainId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByTerrain(terrainId));
    }
    
    @PutMapping("/{id}/status")
    
    public ResponseEntity<Maintenance> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        return ResponseEntity.ok(maintenanceService.updateMaintenanceStatus(id, status));
    }
}

