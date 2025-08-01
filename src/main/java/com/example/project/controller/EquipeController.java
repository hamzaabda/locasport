package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.DTO.EquipeDTO;
import com.example.project.service.EquipeService;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @GetMapping
    public List<EquipeDTO> getAllEquipes() {
        return equipeService.getAllEquipes();
    }

    @GetMapping("/evenement/{evenementId}")
    public List<EquipeDTO> getEquipesByEvenement(@PathVariable Long evenementId) {
        return equipeService.getEquipesByEvenement(evenementId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeDTO> getEquipeById(@PathVariable Long id) {
        EquipeDTO equipeDTO = equipeService.getEquipeById(id);
        return ResponseEntity.ok(equipeDTO);
    }

    @PostMapping
    public ResponseEntity<EquipeDTO> createEquipe(@RequestBody EquipeDTO equipeDTO) {
        EquipeDTO createdEquipe = equipeService.createEquipe(equipeDTO);
        return ResponseEntity.ok(createdEquipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeDTO> updateEquipe(@PathVariable Long id, @RequestBody EquipeDTO equipeDTO) {
        EquipeDTO updatedEquipe = equipeService.updateEquipe(id, equipeDTO);
        return ResponseEntity.ok(updatedEquipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipe(@PathVariable Long id) {
        equipeService.deleteEquipe(id);
        return ResponseEntity.noContent().build();
    }
}
