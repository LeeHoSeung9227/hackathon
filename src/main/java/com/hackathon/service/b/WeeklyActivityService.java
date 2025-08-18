package com.hackathon.service.b;

import com.hackathon.dto.b.WeeklyActivityDto;
import com.hackathon.entity.b.WeeklyActivity;
import com.hackathon.repository.b.WeeklyActivityRepository;
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
public class WeeklyActivityService {
    
    private final WeeklyActivityRepository weeklyActivityRepository;
    
    public WeeklyActivityDto createWeeklyActivity(Long userId, LocalDate weekStartDate, LocalDate weekEndDate, 
                                                Integer totalPoints, Integer activitiesCount) {
        WeeklyActivity activity = new WeeklyActivity();
        activity.setUserId(userId);
        activity.setWeekStartDate(weekStartDate);
        activity.setWeekEndDate(weekEndDate);
        activity.setTotalPoints(totalPoints);
        activity.setActivitiesCount(activitiesCount);
        
        WeeklyActivity savedActivity = weeklyActivityRepository.save(activity);
        return convertToDto(savedActivity);
    }
    
    public WeeklyActivityDto getWeeklyActivityById(Long id) {
        WeeklyActivity activity = weeklyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주간 활동을 찾을 수 없습니다: " + id));
        return convertToDto(activity);
    }
    
    public Optional<WeeklyActivityDto> getWeeklyActivityByUserIdAndWeekStart(Long userId, LocalDate weekStartDate) {
        return weeklyActivityRepository.findByUserIdAndWeekStartDate(userId, weekStartDate)
                .map(this::convertToDto);
    }
    
    public List<WeeklyActivityDto> getWeeklyActivitiesByUserId(Long userId) {
        return weeklyActivityRepository.findByUserIdOrderByWeekStartDateDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<WeeklyActivityDto> getWeeklyActivitiesByWeekStart(LocalDate weekStartDate) {
        return weeklyActivityRepository.findByWeekStartDateOrderByTotalPointsDesc(weekStartDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public WeeklyActivityDto updateWeeklyActivity(Long id, Integer totalPoints, Integer activitiesCount) {
        WeeklyActivity activity = weeklyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주간 활동을 찾을 수 없습니다: " + id));
        
        activity.setTotalPoints(totalPoints);
        activity.setActivitiesCount(activitiesCount);
        
        WeeklyActivity updatedActivity = weeklyActivityRepository.save(activity);
        return convertToDto(updatedActivity);
    }
    
    public void deleteWeeklyActivity(Long id) {
        if (!weeklyActivityRepository.existsById(id)) {
            throw new RuntimeException("주간 활동을 찾을 수 없습니다: " + id);
        }
        weeklyActivityRepository.deleteById(id);
    }
    
    private WeeklyActivityDto convertToDto(WeeklyActivity activity) {
        WeeklyActivityDto dto = new WeeklyActivityDto();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUserId());
        dto.setWeekStartDate(activity.getWeekStartDate());
        dto.setWeekEndDate(activity.getWeekEndDate());
        dto.setTotalPoints(activity.getTotalPoints());
        dto.setActivitiesCount(activity.getActivitiesCount());
        dto.setCreatedAt(activity.getCreatedAt());
        dto.setUpdatedAt(activity.getUpdatedAt());
        return dto;
    }
}
