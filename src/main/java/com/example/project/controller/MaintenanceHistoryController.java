package com.example.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.MaintenanceHistory;
import com.example.project.service.MaintenanceHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maintenance/history")
@RequiredArgsConstructor
public class MaintenanceHistoryController {
    private final MaintenanceHistoryService historyService;
    
    @GetMapping("/{maintenanceId}")
    public ResponseEntity<List<MaintenanceHistory>> getHistory(
            @PathVariable Long maintenanceId) {
        return ResponseEntity.ok(historyService.getMaintenanceHistory(maintenanceId));
    }
}