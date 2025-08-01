package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.DTO.ParticipantDTO;
import com.example.project.model.Equipe;
import com.example.project.model.Participant;
import com.example.project.repository.EquipeRepository;
import com.example.project.repository.ParticipantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ParticipantDTO> getParticipantsByEquipe(Long equipeId) {
        return participantRepository.findByEquipeId(equipeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ParticipantDTO getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        return convertToDTO(participant);
    }

    public ParticipantDTO createParticipant(ParticipantDTO participantDTO) {
        Participant participant = convertToEntity(participantDTO);
        Participant savedParticipant = participantRepository.save(participant);
        return convertToDTO(savedParticipant);
    }

    public ParticipantDTO updateParticipant(Long id, ParticipantDTO participantDTO) {
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        existingParticipant.setNom(participantDTO.getNom());
        existingParticipant.setPrenom(participantDTO.getPrenom());
        existingParticipant.setEmail(participantDTO.getEmail());
        existingParticipant.setTelephone(participantDTO.getTelephone());

        Equipe equipe = equipeRepository.findById(participantDTO.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        existingParticipant.setEquipe(equipe);

        Participant updatedParticipant = participantRepository.save(existingParticipant);
        return convertToDTO(updatedParticipant);
    }

    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    private ParticipantDTO convertToDTO(Participant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setId(participant.getId());
        dto.setNom(participant.getNom());
        dto.setPrenom(participant.getPrenom());
        dto.setEmail(participant.getEmail());
        dto.setTelephone(participant.getTelephone());
        dto.setEquipeId(participant.getEquipe().getId());
        return dto;
    }

    private Participant convertToEntity(ParticipantDTO dto) {
        Participant participant = new Participant();
        participant.setNom(dto.getNom());
        participant.setPrenom(dto.getPrenom());
        participant.setEmail(dto.getEmail());
        participant.setTelephone(dto.getTelephone());

        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        participant.setEquipe(equipe);
        
        return participant;
    }
}