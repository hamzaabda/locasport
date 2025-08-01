package com.example.project.controller;

import com.example.project.DTO.AlerteDto;
import com.example.project.DTO.ResponseDto;
import com.example.project.model.Alerte;
import com.example.project.service.AlerteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@RequiredArgsConstructor
public class AlerteController {

    private final AlerteService alerteService;

    @PostMapping
    public ResponseEntity<ResponseDto> createAlert(@RequestBody AlerteDto alerteDto) {
        return ResponseEntity.ok(alerteService.creerAlerte(alerteDto));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ResponseDto> deactivateAlert(@PathVariable Long id) {
        return ResponseEntity.ok(alerteService.desactiverAlerte(id));
    }

    @GetMapping
    public ResponseEntity<List<Alerte>> getActiveAlerts() {
        return ResponseEntity.ok(alerteService.getAlertesActives());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Alerte>> getAlertsByType(@PathVariable String type) {
        return ResponseEntity.ok(alerteService.getAlertesParType(type));
    }

    @GetMapping("/urgent")
    public ResponseEntity<List<Alerte>> getUrgentAlerts() {
        return ResponseEntity.ok(alerteService.getAlertesUrgentes());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Alerte>> getExpiredAlerts() {
        return ResponseEntity.ok(alerteService.getAlertesExpirees());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateAlert(
            @PathVariable Long id,
            @RequestBody AlerteDto alerteDto) {
        return ResponseEntity.ok(alerteService.mettreAJourAlerte(id, alerteDto));
    }
}