package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.DTO.TarificationDTO;
import com.example.project.model.Evenement;
import com.example.project.model.Tarification;
import com.example.project.repository.EvenementRepository;
import com.example.project.repository.TarificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarificationService {

    @Autowired
    private TarificationRepository tarificationRepository;

    @Autowired
    private EvenementRepository evenementRepository;

    public List<TarificationDTO> getAllTarifications() {
        return tarificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TarificationDTO> getTarificationsByEvenement(Long evenementId) {
        return tarificationRepository.findByEvenementId(evenementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TarificationDTO getTarificationById(Long id) {
        Tarification tarification = tarificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarification non trouvée"));
        return convertToDTO(tarification);
    }

    public TarificationDTO createTarification(TarificationDTO tarificationDTO) {
        Tarification tarification = convertToEntity(tarificationDTO);
        Tarification savedTarification = tarificationRepository.save(tarification);
        return convertToDTO(savedTarification);
    }

    public TarificationDTO updateTarification(Long id, TarificationDTO tarificationDTO) {
        Tarification existingTarification = tarificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarification non trouvée"));

        existingTarification.setType(tarificationDTO.getType());
        existingTarification.setPrix(tarificationDTO.getPrix());
        existingTarification.setDescription(tarificationDTO.getDescription());

        Evenement evenement = evenementRepository.findById(tarificationDTO.getEvenementId())
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        existingTarification.setEvenement(evenement);

        Tarification updatedTarification = tarificationRepository.save(existingTarification);
        return convertToDTO(updatedTarification);
    }

    public void deleteTarification(Long id) {
        tarificationRepository.deleteById(id);
    }

    private TarificationDTO convertToDTO(Tarification tarification) {
        TarificationDTO dto = new TarificationDTO();
        dto.setId(tarification.getId());
        dto.setType(tarification.getType());
        dto.setPrix(tarification.getPrix());
        dto.setDescription(tarification.getDescription());
        dto.setEvenementId(tarification.getEvenement().getId());
        return dto;
    }

    private Tarification convertToEntity(TarificationDTO dto) {
        Tarification tarification = new Tarification();
        tarification.setType(dto.getType());
        tarification.setPrix(dto.getPrix());
        tarification.setDescription(dto.getDescription());

        Evenement evenement = evenementRepository.findById(dto.getEvenementId())
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        tarification.setEvenement(evenement);
        
        return tarification;
    }
}
