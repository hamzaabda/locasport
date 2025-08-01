package com.example.project.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.DTO.AlerteDto;
import com.example.project.DTO.ResponseDto;
import com.example.project.exception.AlerteNotFoundException;
import com.example.project.model.Alerte;
import com.example.project.repository.AlerteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlerteService {
    
    private final AlerteRepository alerteRepository;

  

    public ResponseDto creerAlerte(AlerteDto alerteDto) {
        Alerte alerte = new Alerte();
        alerte.setType(alerteDto.getType());
        alerte.setTitre(alerteDto.getTitre());
        alerte.setDescription(alerteDto.getDescription());
        alerte.setDateCreation(LocalDateTime.now());
        alerte.setDateExpiration(alerteDto.getDateExpiration());
        alerte.setNiveauUrgence(alerteDto.getNiveauUrgence());
        alerte.setActive(true);
        
        Alerte savedAlerte = alerteRepository.save(alerte);
        
        return new ResponseDto("SUCCESS", "Alerte créée avec succès", savedAlerte);
    }

    public ResponseDto desactiverAlerte(Long id) {
        Alerte alerte = alerteRepository.findById(id)
                .orElseThrow(() -> new AlerteNotFoundException("Alerte non trouvée"));
        alerte.setActive(false);
        alerteRepository.save(alerte);
        return new ResponseDto("SUCCESS", "Alerte désactivée avec succès");
    }

    public List<Alerte> getAlertesActives() {
        return alerteRepository.findByActiveTrueAndDateExpirationAfter(LocalDateTime.now());
    }

    public List<Alerte> getAlertesParType(String type) {
        return alerteRepository.findByTypeAndActiveTrue(type);
    }

    public List<Alerte> getAlertesUrgentes() {
        return alerteRepository.findByNiveauUrgenceAndActiveTrue("URGENT");
    }

    // Méthode alternative pour les alertes expirées
    public List<Alerte> getAlertesExpirees() {
        return alerteRepository.findByActiveTrueAndDateExpirationBefore(LocalDateTime.now());
    }

    public ResponseDto mettreAJourAlerte(Long id, AlerteDto alerteDto) {
        Alerte alerte = alerteRepository.findById(id)
                .orElseThrow(() -> new AlerteNotFoundException("Alerte non trouvée"));
        
        alerte.setType(alerteDto.getType());
        alerte.setTitre(alerteDto.getTitre());
        alerte.setDescription(alerteDto.getDescription());
        alerte.setDateExpiration(alerteDto.getDateExpiration());
        alerte.setNiveauUrgence(alerteDto.getNiveauUrgence());
        
        alerteRepository.save(alerte);
        
        return new ResponseDto("SUCCESS", "Alerte mise à jour avec succès");
    }
}