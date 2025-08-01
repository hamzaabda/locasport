package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.DTO.EquipeDTO;
import com.example.project.model.Equipe;
import com.example.project.model.Evenement;
import com.example.project.repository.EquipeRepository;
import com.example.project.repository.EvenementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EvenementRepository evenementRepository;

    public List<EquipeDTO> getAllEquipes() {
        return equipeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipeDTO> getEquipesByEvenement(Long evenementId) {
        return equipeRepository.findByEvenementId(evenementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EquipeDTO getEquipeById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        return convertToDTO(equipe);
    }

    public EquipeDTO createEquipe(EquipeDTO equipeDTO) {
        Equipe equipe = convertToEntity(equipeDTO);
        Equipe savedEquipe = equipeRepository.save(equipe);
        return convertToDTO(savedEquipe);
    }

    public EquipeDTO updateEquipe(Long id, EquipeDTO equipeDTO) {
        Equipe existingEquipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

        existingEquipe.setNom(equipeDTO.getNom());
        
        Evenement evenement = evenementRepository.findById(equipeDTO.getEvenementId())
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        existingEquipe.setEvenement(evenement);

        Equipe updatedEquipe = equipeRepository.save(existingEquipe);
        return convertToDTO(updatedEquipe);
    }

    public void deleteEquipe(Long id) {
        equipeRepository.deleteById(id);
    }

    private EquipeDTO convertToDTO(Equipe equipe) {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(equipe.getId());
        dto.setNom(equipe.getNom());
        dto.setEvenementId(equipe.getEvenement().getId());
        return dto;
    }

    private Equipe convertToEntity(EquipeDTO dto) {
        Equipe equipe = new Equipe();
        equipe.setNom(dto.getNom());
        
        Evenement evenement = evenementRepository.findById(dto.getEvenementId())
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        equipe.setEvenement(evenement);
        
        return equipe;
    }
}
