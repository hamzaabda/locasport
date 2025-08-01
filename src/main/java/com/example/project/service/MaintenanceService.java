package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.model.Maintenance;
import com.example.project.repository.MaintenanceRepository;
import com.example.project.repository.TerrainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final TerrainRepository terrainRepository;
    
    public Maintenance scheduleMaintenance(Maintenance maintenance) {
        // Validation supplémentaire
        if (maintenance.getTerrain() == null || !terrainRepository.existsById(maintenance.getTerrain().getId())) {
            throw new IllegalArgumentException("Invalid terrain reference");
        }
        
        maintenance.setStatus("PLANNED");
        return maintenanceRepository.save(maintenance);
    }
    
    public List<Maintenance> getMaintenanceByTerrain(Long terrainId) {
        return maintenanceRepository.findByTerrainId(terrainId);
    }
    
    public Maintenance updateMaintenanceStatus(Long id, String status) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow();
        maintenance.setStatus(status);
        if ("COMPLETED".equals(status)) {
            maintenance.setEndDate(LocalDateTime.now());
        }
        return maintenanceRepository.save(maintenance);
    }
}