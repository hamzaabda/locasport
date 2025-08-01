package com.example.project.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Evenement;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
}