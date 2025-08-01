package com.example.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.model.MaintenanceHistory;
import com.example.project.repository.MaintenanceHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceHistoryService {
    private final MaintenanceHistoryRepository historyRepository;
    
    public MaintenanceHistory addHistoryRecord(MaintenanceHistory history) {
        return historyRepository.save(history);
    }
    
    public List<MaintenanceHistory> getMaintenanceHistory(Long maintenanceId) {
        return historyRepository.findByMaintenanceId(maintenanceId);
    }
}
