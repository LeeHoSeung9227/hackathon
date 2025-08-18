package com.hackathon.service;

import com.hackathon.dto.ActivityHistoryDto;
import com.hackathon.entity.ActivityHistory;
import com.hackathon.repository.ActivityHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityHistoryService {
    
    private final ActivityHistoryRepository activityHistoryRepository;
    
    public ActivityHistoryService(ActivityHistoryRepository activityHistoryRepository) {
        this.activityHistoryRepository = activityHistoryRepository;
    }
    
    /**
     * 새로운 활동 기록 생성
     */
    public ActivityHistoryDto createActivity(Long userId, LocalDate activityDate, String activityType, 
                                           String activityName, Integer points, Long badgeId) {
        ActivityHistory activity = new ActivityHistory();
        activity.setUserId(userId);
        activity.setActivityDate(activityDate);
        activity.setActivityType(activityType);
        activity.setActivityName(activityName);
        activity.setPoints(points);
        activity.setBadgeId(badgeId);
        activity.setCreatedAt(LocalDateTime.now());
        
        ActivityHistory saved = activityHistoryRepository.save(activity);
        return convertToDto(saved);
    }
    
    /**
     * 활동 기록 조회
     */
    public Optional<ActivityHistoryDto> getActivity(Long id) {
        return activityHistoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 활동 기록 조회
     */
    public List<ActivityHistoryDto> getUserActivities(Long userId) {
        return activityHistoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 특정 날짜 활동 기록 조회
     */
    public List<ActivityHistoryDto> getUserActivitiesByDate(Long userId, LocalDate activityDate) {
        return activityHistoryRepository.findByUserIdAndActivityDate(userId, activityDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 활동 타입별 기록 조회
     */
    public List<ActivityHistoryDto> getUserActivitiesByType(Long userId, String activityType) {
        return activityHistoryRepository.findByUserIdAndActivityType(userId, activityType)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 날짜 범위별 활동 기록 조회
     */
    public List<ActivityHistoryDto> getUserActivitiesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return activityHistoryRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 특정 날짜 포인트 합계 조회
     */
    public Integer getPointsByDate(Long userId, LocalDate date) {
        Integer totalPoints = activityHistoryRepository.sumPointsByUserIdAndDate(userId, date);
        return totalPoints != null ? totalPoints : 0;
    }
    
    /**
     * 활동 기록 삭제
     */
    public void deleteActivity(Long id) {
        activityHistoryRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private ActivityHistoryDto convertToDto(ActivityHistory activity) {
        ActivityHistoryDto dto = new ActivityHistoryDto();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUserId());
        dto.setActivityDate(activity.getActivityDate());
        dto.setActivityType(activity.getActivityType());
        dto.setActivityName(activity.getActivityName());
        dto.setPoints(activity.getPoints());
        dto.setBadgeId(activity.getBadgeId());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
    }
}
