package com.example.project.model;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
  
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED
    private String technicianNotes;


    @ManyToOne
    @JoinColumn(name = "terrain_id", nullable = false) // Ajoutez cette annotation
    private Terrain terrain;


}

