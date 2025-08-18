package com.hackathon.controller.b;

import com.hackathon.dto.ActivityHistoryDto;
import com.hackathon.dto.DailyActivityDto;
import com.hackathon.dto.WeeklyActivityDto;
import com.hackathon.entity.ActivityHistory;
import com.hackathon.entity.DailyActivity;
import com.hackathon.entity.WeeklyActivity;
import com.hackathon.repository.ActivityHistoryRepository;
import com.hackathon.repository.DailyActivityRepository;
import com.hackathon.repository.WeeklyActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
@CrossOrigin(origins = "*")
public class ActivityController {
    
    private final ActivityHistoryRepository activityHistoryRepository;
    private final DailyActivityRepository dailyActivityRepository;
    private final WeeklyActivityRepository weeklyActivityRepository;
    
    public ActivityController(ActivityHistoryRepository activityHistoryRepository, 
                           DailyActivityRepository dailyActivityRepository,
                           WeeklyActivityRepository weeklyActivityRepository) {
        this.activityHistoryRepository = activityHistoryRepository;
        this.dailyActivityRepository = dailyActivityRepository;
        this.weeklyActivityRepository = weeklyActivityRepository;
    }
    
    // ===== 활동 기록 =====
    
    /**
     * 사용자 활동 기록 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserActivities(@PathVariable Long userId) {
        try {
            List<ActivityHistory> activities = activityHistoryRepository.findByUserId(userId);
            
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
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdAndActivityDate(userId, activityDate);
            
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
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdAndActivityType(userId, activityType);
            
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
            
            List<ActivityHistory> activities = activityHistoryRepository.findByUserIdAndActivityDateBetween(userId, start, end);
            
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
            List<DailyActivity> dailyActivities = dailyActivityRepository.findByUserId(userId);
            
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
            List<WeeklyActivity> weeklyActivities = weeklyActivityRepository.findByUserId(userId);
            
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
            var weeklyActivity = weeklyActivityRepository.findByUserIdAndWeekOfYear(userId, weekOfYear);
            
            if (weeklyActivity.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToWeeklyDto(weeklyActivity.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "해당 주차의 주간 활동을 찾을 수 없습니다."
                ));
            }
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
        dto.setActivityDate(activity.getActivityDate());
        dto.setPoints(activity.getPoints());
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
        dto.setActivityCount(daily.getActivityCount());
        return dto;
    }
    
    private WeeklyActivityDto convertToWeeklyDto(WeeklyActivity weekly) {
        WeeklyActivityDto dto = new WeeklyActivityDto();
        dto.setId(weekly.getId());
        dto.setUserId(weekly.getUserId());
        dto.setWeekOfYear(weekly.getWeekOfYear());
        dto.setTotalPoints(weekly.getTotalPoints());
        dto.setActivityCount(weekly.getActivityCount());
        return dto;
    }
}
