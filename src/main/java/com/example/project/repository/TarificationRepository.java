package com.example.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Tarification;

import java.util.List;

@Repository
public interface TarificationRepository extends JpaRepository<Tarification, Long> {
    List<Tarification> findByEvenementId(Long evenementId);
}
