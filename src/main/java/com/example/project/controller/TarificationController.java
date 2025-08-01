package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.DTO.TarificationDTO;
import com.example.project.service.TarificationService;

import java.util.List;

@RestController
@RequestMapping("/api/tarifications")
public class TarificationController {

    @Autowired
    private TarificationService tarificationService;

    @GetMapping
    public List<TarificationDTO> getAllTarifications() {
        return tarificationService.getAllTarifications();
    }

    @GetMapping("/evenement/{evenementId}")
    public List<TarificationDTO> getTarificationsByEvenement(@PathVariable Long evenementId) {
        return tarificationService.getTarificationsByEvenement(evenementId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarificationDTO> getTarificationById(@PathVariable Long id) {
        TarificationDTO tarificationDTO = tarificationService.getTarificationById(id);
        return ResponseEntity.ok(tarificationDTO);
    }

    @PostMapping
    public ResponseEntity<TarificationDTO> createTarification(@RequestBody TarificationDTO tarificationDTO) {
        TarificationDTO createdTarification = tarificationService.createTarification(tarificationDTO);
        return ResponseEntity.ok(createdTarification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarificationDTO> updateTarification(@PathVariable Long id, @RequestBody TarificationDTO tarificationDTO) {
        TarificationDTO updatedTarification = tarificationService.updateTarification(id, tarificationDTO);
        return ResponseEntity.ok(updatedTarification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarification(@PathVariable Long id) {
        tarificationService.deleteTarification(id);
        return ResponseEntity.noContent().build();
    }
}