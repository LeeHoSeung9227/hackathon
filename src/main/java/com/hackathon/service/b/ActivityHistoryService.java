package com.hackathon.service.b;

import com.hackathon.dto.b.ActivityHistoryDto;
import com.hackathon.entity.b.ActivityHistory;
import com.hackathon.repository.b.ActivityHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityHistoryService {
    
    private final ActivityHistoryRepository activityHistoryRepository;
    
    public ActivityHistoryDto createActivityHistory(Long userId, String activityType, String description, Integer pointsEarned) {
        ActivityHistory history = new ActivityHistory();
        history.setUserId(userId);
        history.setActivityType(activityType);
        history.setDescription(description);
        history.setPointsEarned(pointsEarned);
        
        ActivityHistory savedHistory = activityHistoryRepository.save(history);
        return convertToDto(savedHistory);
    }
    
    public ActivityHistoryDto getActivityHistoryById(Long id) {
        ActivityHistory history = activityHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("활동 내역을 찾을 수 없습니다: " + id));
        return convertToDto(history);
    }
    
    public List<ActivityHistoryDto> getAllActivityHistory() {
        return activityHistoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ActivityHistoryDto> getActivityHistoryByUserId(Long userId) {
        return activityHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ActivityHistoryDto> getActivityHistoryByType(String activityType) {
        return activityHistoryRepository.findByActivityTypeOrderByCreatedAtDesc(activityType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ActivityHistoryDto> getActivityHistoryByUserIdAndType(Long userId, String activityType) {
        return activityHistoryRepository.findByUserIdAndActivityTypeOrderByCreatedAtDesc(userId, activityType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteActivityHistory(Long id) {
        if (!activityHistoryRepository.existsById(id)) {
            throw new RuntimeException("활동 내역을 찾을 수 없습니다: " + id);
        }
        activityHistoryRepository.deleteById(id);
    }
    
    private ActivityHistoryDto convertToDto(ActivityHistory history) {
        ActivityHistoryDto dto = new ActivityHistoryDto();
        dto.setId(history.getId());
        dto.setUserId(history.getUserId());
        dto.setActivityType(history.getActivityType());
        dto.setDescription(history.getDescription());
        dto.setPointsEarned(history.getPointsEarned());
        dto.setCreatedAt(history.getCreatedAt());
        dto.setUpdatedAt(history.getUpdatedAt());
        return dto;
    }
}
