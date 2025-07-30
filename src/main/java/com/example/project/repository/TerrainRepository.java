package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.Terrain;

public interface TerrainRepository extends JpaRepository<Terrain, Long> {
    List<Terrain> findByType(Terrain.TypeTerrain type);
    List<Terrain> findByEstDisponible(boolean estDisponible);
}