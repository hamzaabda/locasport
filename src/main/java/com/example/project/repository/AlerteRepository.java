package com.example.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Alerte;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {
   

    List<Alerte> findByActiveTrueAndDateExpirationAfter(LocalDateTime now);
    List<Alerte> findByTypeAndActiveTrue(String type);
    List<Alerte> findByNiveauUrgenceAndActiveTrue(String niveauUrgence);
    List<Alerte> findByActiveTrueAndDateExpirationBefore(LocalDateTime now); 






}
