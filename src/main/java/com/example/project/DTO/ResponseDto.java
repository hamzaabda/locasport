package com.example.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
    private Object data;

    // Constructeur supplémentaire sans le champ data
    public ResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}