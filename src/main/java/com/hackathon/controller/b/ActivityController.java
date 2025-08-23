package com.hackathon.controller.b;

import com.hackathon.dto.b.ActivityHistoryDto;
import com.hackathon.dto.b.DailyActivityDto;
import com.hackathon.dto.b.WeeklyActivityDto;
import com.hackathon.entity.b.ActivityHistory;
import com.hackathon.entity.b.DailyActivity;
import com.hackathon.entity.b.WeeklyActivity;
import com.hackathon.repository.b.ActivityHistoryRepository;
import com.hackathon.repository.b.DailyActivityRepository;
import com.hackathon.repository.b.WeeklyActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    
    private final ActivityHistoryRepository activityHistoryRepository;
    private final DailyActivityRepository dailyActivityRepository;
    private final WeeklyActivityRepository weeklyActivityRepository;
    
    // ===== 활동 기록 =====
    
    /**
     * 사용자 활동 기록 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserActivities(@PathVariable Long userId) {
        try {
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
            
            List<ActivityHistoryDto> activityDtos = activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", activityDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 특정 날짜 활동 기록 조회
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<Map<String, Object>> getUserActivitiesByDate(
            @PathVariable Long userId, 
            @PathVariable String date) {
        try {
            LocalDate activityDate = LocalDate.parse(date);
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
            
            List<ActivityHistoryDto> activityDtos = activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", activityDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 활동 타입별 기록 조회
     */
    @GetMapping("/user/{userId}/type/{activityType}")
    public ResponseEntity<Map<String, Object>> getUserActivitiesByType(
            @PathVariable Long userId, 
            @PathVariable String activityType) {
        try {
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdAndActivityTypeOrderByCreatedAtDesc(userId, activityType);
            
            List<ActivityHistoryDto> activityDtos = activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", activityDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 활동 기록 조회
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserActivitiesByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
            
            List<ActivityHistoryDto> activityDtos = activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", activityDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== 일간 활동 =====
    
    /**
     * 사용자 일간 활동 조회
     */
    @GetMapping("/user/{userId}/daily")
    public ResponseEntity<Map<String, Object>> getUserDailyActivities(@PathVariable Long userId) {
        try {
            List<DailyActivity> dailyActivities = dailyActivityRepository.findByUserIdOrderByActivityDateDesc(userId);
            
            List<DailyActivityDto> dailyDtos = dailyActivities.stream()
                .map(this::convertToDailyDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", dailyDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 특정 날짜 일간 활동 조회
     */
    @GetMapping("/user/{userId}/daily/{date}")
    public ResponseEntity<Map<String, Object>> getUserDailyActivityByDate(
            @PathVariable Long userId, 
            @PathVariable String date) {
        try {
            LocalDate activityDate = LocalDate.parse(date);
            var dailyActivity = dailyActivityRepository.findByUserIdAndActivityDate(userId, activityDate);
            
            if (dailyActivity.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToDailyDto(dailyActivity.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "해당 날짜의 일간 활동을 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== 주간 활동 =====
    
    /**
     * 사용자 주간 활동 조회
     */
    @GetMapping("/user/{userId}/weekly")
    public ResponseEntity<Map<String, Object>> getUserWeeklyActivities(@PathVariable Long userId) {
        try {
            List<WeeklyActivity> weeklyActivities = weeklyActivityRepository.findByUserIdOrderByWeekStartDateDesc(userId);
            
            List<WeeklyActivityDto> weeklyDtos = weeklyActivities.stream()
                .map(this::convertToWeeklyDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", weeklyDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 특정 주차 주간 활동 조회
     */
    @GetMapping("/user/{userId}/weekly/{weekOfYear}")
    public ResponseEntity<Map<String, Object>> getUserWeeklyActivityByWeek(
            @PathVariable Long userId, 
            @PathVariable Integer weekOfYear) {
        try {
            return ResponseEntity.badRequest().body(Map.of("error", "weekOfYear 기반 조회는 지원하지 않습니다. 주 시작일을 사용하세요."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private ActivityHistoryDto convertToDto(ActivityHistory activity) {
        ActivityHistoryDto dto = new ActivityHistoryDto();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUserId());
        dto.setActivityType(activity.getActivityType());
        // ActivityHistory에는 activityDate 필드가 없으므로 createdAt 사용
        dto.setPointsEarned(activity.getPointsEarned());
        dto.setDescription(activity.getDescription());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
    }
    
    private DailyActivityDto convertToDailyDto(DailyActivity daily) {
        DailyActivityDto dto = new DailyActivityDto();
        dto.setId(daily.getId());
        dto.setUserId(daily.getUserId());
        dto.setActivityDate(daily.getActivityDate());
        dto.setTotalPoints(daily.getTotalPoints());
        dto.setActivitiesCount(daily.getActivitiesCount());
        return dto;
    }
    
    private WeeklyActivityDto convertToWeeklyDto(WeeklyActivity weekly) {
        WeeklyActivityDto dto = new WeeklyActivityDto();
        dto.setId(weekly.getId());
        dto.setUserId(weekly.getUserId());
        dto.setWeekStartDate(weekly.getWeekStartDate());
        dto.setWeekEndDate(weekly.getWeekEndDate());
        dto.setTotalPoints(weekly.getTotalPoints());
        dto.setActivitiesCount(weekly.getActivitiesCount());
        return dto;
    }
}
