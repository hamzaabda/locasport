package com.example.project.DTO;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlerteDto {
    private String type;
    private String titre;
    private String description;
    private LocalDateTime dateExpiration;
    private String niveauUrgence;
}