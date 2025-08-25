package com.hackathon.service.b;

import com.hackathon.dto.b.DailyActivityDto;
import com.hackathon.entity.b.DailyActivity;
import com.hackathon.repository.b.DailyActivityRepository;
import com.hackathon.service.a.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyActivityService {
    
    private final DailyActivityRepository dailyActivityRepository;
    private final PointHistoryService pointHistoryService;
    private final BadgeService badgeService;
    
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
    
    /**
     * 특정 날짜의 사용자 활동 통계 조회 (포인트 히스토리 + 뱃지 기반)
     */
    public Map<String, Object> getDailyActivityStatsByUserIdAndDate(Long userId, LocalDate activityDate) {
        try {
            // 해당 날짜의 시작과 끝 시간 설정
            LocalDateTime startOfDay = activityDate.atStartOfDay();
            LocalDateTime endOfDay = activityDate.atTime(23, 59, 59);
            
            // 해당 날짜의 AI 분석 포인트 히스토리 조회
            List<com.hackathon.dto.a.PointHistoryDto> dailyHistory = pointHistoryService.getPointHistoryByUserId(userId)
                .stream()
                .filter(history -> {
                    LocalDateTime historyDate = history.getCreatedAt();
                    return historyDate != null && 
                           historyDate.isAfter(startOfDay) && 
                           historyDate.isBefore(endOfDay);
                })
                .collect(Collectors.toList());
            
            // AI 분석 타입만 필터링 (분리수거 활동)
            List<com.hackathon.dto.a.PointHistoryDto> aiAnalysisHistory = dailyHistory
                .stream()
                .filter(history -> "AI_ANALYSIS".equals(history.getType()))
                .collect(Collectors.toList());
            
            // 뱃지 획득 히스토리 조회
            List<com.hackathon.dto.a.PointHistoryDto> badgeHistory = dailyHistory
                .stream()
                .filter(history -> "BADGE_EARNED".equals(history.getType()))
                .collect(Collectors.toList());
            
            // 통계 계산
            int recyclingCount = aiAnalysisHistory.size(); // 분리수거 횟수
            int totalPointsEarned = aiAnalysisHistory.stream() // 그날 얻은 포인트
                .mapToInt(history -> history.getPoints() != null ? history.getPoints() : 0)
                .sum();
            int badgesEarned = badgeHistory.size(); // 그날 얻은 뱃지 수
            
            // DailyActivity 생성 또는 업데이트
            Optional<DailyActivity> existingActivity = dailyActivityRepository.findByUserIdAndActivityDate(userId, activityDate);
            
            DailyActivity activity;
            if (existingActivity.isPresent()) {
                // 기존 활동 업데이트
                activity = existingActivity.get();
                activity.setTotalPoints(totalPointsEarned);
                activity.setActivitiesCount(recyclingCount);
                activity.setUpdatedAt(LocalDateTime.now());
            } else {
                // 새 활동 생성
                activity = new DailyActivity();
                activity.setUserId(userId);
                activity.setActivityDate(activityDate);
                activity.setTotalPoints(totalPointsEarned);
                activity.setActivitiesCount(recyclingCount);
                activity.setCreatedAt(LocalDateTime.now());
                activity.setUpdatedAt(LocalDateTime.now());
            }
            
            DailyActivity savedActivity = dailyActivityRepository.save(activity);
            
            // 뱃지 상세 정보 조회
            List<Map<String, Object>> badgeDetails = badgeHistory.stream()
                .map(badge -> {
                    Map<String, Object> badgeInfo = new HashMap<>();
                    badgeInfo.put("description", badge.getDescription());
                    badgeInfo.put("points", badge.getPoints());
                    badgeInfo.put("earnedAt", badge.getCreatedAt());
                    return badgeInfo;
                })
                .collect(Collectors.toList());
            
            // 통합 결과 반환
            Map<String, Object> result = new HashMap<>();
            result.put("dailyActivity", convertToDto(savedActivity));
            result.put("recyclingCount", recyclingCount);
            result.put("totalPointsEarned", totalPointsEarned);
            result.put("badgesEarned", badgesEarned);
            result.put("badgeDetails", badgeDetails);
            result.put("date", activityDate);
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("일일 활동 통계 조회 중 오류 발생: " + e.getMessage(), e);
        }
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
