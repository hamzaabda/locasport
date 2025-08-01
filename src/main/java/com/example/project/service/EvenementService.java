package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.DTO.EvenementDTO;
import com.example.project.model.Evenement;
import com.example.project.repository.EvenementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    public List<EvenementDTO> getAllEvenements() {
        return evenementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EvenementDTO getEvenementById(Long id) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        return convertToDTO(evenement);
    }

    public EvenementDTO createEvenement(EvenementDTO evenementDTO) {
        Evenement evenement = convertToEntity(evenementDTO);
        Evenement savedEvenement = evenementRepository.save(evenement);
        return convertToDTO(savedEvenement);
    }

    public EvenementDTO updateEvenement(Long id, EvenementDTO evenementDTO) {
        Evenement existingEvenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        existingEvenement.setNom(evenementDTO.getNom());
        existingEvenement.setDescription(evenementDTO.getDescription());
        existingEvenement.setDateDebut(evenementDTO.getDateDebut());
        existingEvenement.setDateFin(evenementDTO.getDateFin());
        existingEvenement.setLieu(evenementDTO.getLieu());
        existingEvenement.setCapaciteMax(evenementDTO.getCapaciteMax());
        existingEvenement.setEstPublic(evenementDTO.isEstPublic());

        Evenement updatedEvenement = evenementRepository.save(existingEvenement);
        return convertToDTO(updatedEvenement);
    }

    public void deleteEvenement(Long id) {
        evenementRepository.deleteById(id);
    }

    private EvenementDTO convertToDTO(Evenement evenement) {
        EvenementDTO dto = new EvenementDTO();
        dto.setId(evenement.getId());
        dto.setNom(evenement.getNom());
        dto.setDescription(evenement.getDescription());
        dto.setDateDebut(evenement.getDateDebut());
        dto.setDateFin(evenement.getDateFin());
        dto.setLieu(evenement.getLieu());
        dto.setCapaciteMax(evenement.getCapaciteMax());
        dto.setEstPublic(evenement.isEstPublic());
        return dto;
    }

    private Evenement convertToEntity(EvenementDTO dto) {
        Evenement evenement = new Evenement();
        evenement.setNom(dto.getNom());
        evenement.setDescription(dto.getDescription());
        evenement.setDateDebut(dto.getDateDebut());
        evenement.setDateFin(dto.getDateFin());
        evenement.setLieu(dto.getLieu());
        evenement.setCapaciteMax(dto.getCapaciteMax());
        evenement.setEstPublic(dto.isEstPublic());
        return evenement;
    }
}