package com.hackathon.service.b;

import com.hackathon.dto.b.DailyActivityDto;
import com.hackathon.entity.b.DailyActivity;
import com.hackathon.repository.b.DailyActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyActivityService {
    
    private final DailyActivityRepository dailyActivityRepository;
    
    public DailyActivityDto createDailyActivity(Long userId, LocalDate activityDate, Integer totalPoints, Integer activitiesCount) {
        DailyActivity activity = new DailyActivity();
        activity.setUserId(userId);
        activity.setActivityDate(activityDate);
        activity.setTotalPoints(totalPoints);
        activity.setActivitiesCount(activitiesCount);
        
        DailyActivity savedActivity = dailyActivityRepository.save(activity);
        return convertToDto(savedActivity);
    }
    
    public DailyActivityDto getDailyActivityById(Long id) {
        DailyActivity activity = dailyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일일 활동을 찾을 수 없습니다: " + id));
        return convertToDto(activity);
    }
    
    public Optional<DailyActivityDto> getDailyActivityByUserIdAndDate(Long userId, LocalDate activityDate) {
        return dailyActivityRepository.findByUserIdAndActivityDate(userId, activityDate)
                .map(this::convertToDto);
    }
    
    public List<DailyActivityDto> getDailyActivitiesByUserId(Long userId) {
        return dailyActivityRepository.findByUserIdOrderByActivityDateDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<DailyActivityDto> getDailyActivitiesByDate(LocalDate activityDate) {
        return dailyActivityRepository.findByActivityDateOrderByTotalPointsDesc(activityDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public DailyActivityDto updateDailyActivity(Long id, Integer totalPoints, Integer activitiesCount) {
        DailyActivity activity = dailyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일일 활동을 찾을 수 없습니다: " + id));
        
        activity.setTotalPoints(totalPoints);
        activity.setActivitiesCount(activitiesCount);
        
        DailyActivity updatedActivity = dailyActivityRepository.save(activity);
        return convertToDto(updatedActivity);
    }
    
    public void deleteDailyActivity(Long id) {
        if (!dailyActivityRepository.existsById(id)) {
            throw new RuntimeException("일일 활동을 찾을 수 없습니다: " + id);
        }
        dailyActivityRepository.deleteById(id);
    }
    
    private DailyActivityDto convertToDto(DailyActivity activity) {
        DailyActivityDto dto = new DailyActivityDto();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUserId());
        dto.setActivityDate(activity.getActivityDate());
        dto.setTotalPoints(activity.getTotalPoints());
        dto.setActivitiesCount(activity.getActivitiesCount());
        dto.setCreatedAt(activity.getCreatedAt());
        dto.setUpdatedAt(activity.getUpdatedAt());
        return dto;
    }
}
