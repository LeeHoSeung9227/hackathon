package com.hackathon.controller.a;

import com.hackathon.dto.a.PointHistoryDto;
import com.hackathon.service.a.PointHistoryService;
import com.hackathon.service.a.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    
    private final PointHistoryService pointHistoryService;
    private final UserService userService;
    
    // ===== 포인트 추가/차감 =====
    
    /**
     * 포인트 추가/차감
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPoints(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer points = Integer.valueOf(request.get("points").toString());
            String type = (String) request.get("type");
            String description = (String) request.get("description");
            
            // 포인트 히스토리 저장
            PointHistoryDto pointHistory = pointHistoryService.createPointHistory(userId, type, points, description);
            
            // 사용자 총 포인트 업데이트
            userService.updateUserPoints(userId, points);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", points > 0 ? "포인트가 추가되었습니다." : "포인트가 차감되었습니다.",
                "data", Map.of(
                    "pointHistory", pointHistory,
                    "pointsChanged", points,
                    "newTotalPoints", userService.getUserById(userId).getPointsTotal()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 포인트 요약 정보 (총 포인트, 현재 포인트, 사용 포인트, 단과대 총 포인트)
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserPointSummary(@PathVariable Long userId) {
        try {
            // 사용자 정보 조회
            var user = userService.getUserById(userId);
            
            // 포인트 히스토리 조회
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // 포인트 계산
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // 단과대 총 포인트 (같은 단과대 사용자들의 포인트 합계)
            int collegeTotalPoints = userService.getCollegeTotalPoints(user.getCollege());
            
            // 레벨 정보
            String levelName = getLevelName(user.getLevel());
            
            Map<String, Object> summary = Map.of(
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "college", user.getCollege(),
                    "campus", user.getCampus(),
                    "level", user.getLevel(),
                    "levelName", levelName
                ),
                "points", Map.of(
                    "totalEarned", totalEarned,      // 총 획득 포인트
                    "totalSpent", totalSpent,        // 총 사용 포인트
                    "currentPoints", currentPoints,  // 현재 보유 포인트
                    "collegeTotalPoints", collegeTotalPoints  // 단과대 총 포인트
                ),
                "recentHistory", pointHistory.stream()
                    .limit(5)
                    .collect(Collectors.toList())
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", summary
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // ===== 포인트 내역 =====
    
    /**
     * 사용자 포인트 내역 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPointHistory(@PathVariable Long userId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            List<PointHistoryDto> pointDtos = pointHistory.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pointDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 이미지별 포인트 내역 조회
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getImagePointHistory(@PathVariable Long imagesId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByImageId(imagesId);
            
            // 총 포인트 계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "imageId", imagesId,
                    "totalPoints", totalPoints,
                    "history", pointHistory
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 변경 타입별 포인트 내역 조회
     */
    @GetMapping("/user/{userId}/type/{changeType}")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByType(
            @PathVariable Long userId, 
            @PathVariable String changeType) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserIdAndType(userId, changeType);
            
            // 총 포인트 계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            List<PointHistoryDto> pointDtos = pointHistory.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "userId", userId,
                    "changeType", changeType,
                    "totalPoints", totalPoints,  // 총 포인트 추가
                    "history", pointDtos         // 상세 내역
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 포인트 내역 조회
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 날짜 파싱 (간단한 구현)
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            
            // 포인트 히스토리 조회 (기간별)
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId).stream()
                .filter(ph -> ph.getCreatedAt().isAfter(start) && ph.getCreatedAt().isBefore(end))
                .collect(Collectors.toList());
            
            // 총 포인트 계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "userId", userId,
                    "startDate", startDate,
                    "endDate", endDate,
                    "totalPoints", totalPoints,  // 총 포인트 추가
                    "history", pointHistory      // 상세 내역
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식)" + e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }

    private String getLevelName(int level) {
        switch (level) {
            case 1:
                return "씨앗";
            case 2:
                return "작은 새싹";
            case 3:
                return "새싹";
            case 4:
                return "큰 새싹";
            case 5:
                return "나무";
            default:
                return "씨앗";
        }
    }
}
