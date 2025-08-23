package com.hackathon.controller.common;

import com.hackathon.dto.a.UserDto;
import com.hackathon.dto.common.WasteRecordDto;
import com.hackathon.service.a.UserService;
import com.hackathon.service.common.WasteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final WasteRecordService wasteRecordService;

    /**
     * 사용자 대시보드 정보
     */
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Map<String, Object>> getDashboard(@PathVariable Long userId) {
        try {
            UserDto user = userService.getUserById(userId);
            List<WasteRecordDto> recentRecords = wasteRecordService.getWasteRecordsByUserId(userId).stream()
                .limit(5)
                .collect(Collectors.toList());
            
            // 다음 레벨까지 필요한 포인트 계산
            int currentLevel = user.getLevel();
            int currentPoints = user.getPointsTotal();
            int nextLevelPoints = calculateNextLevelPoints(currentLevel);
            int pointsNeeded = nextLevelPoints - currentPoints;
            
            Map<String, Object> dashboard = Map.of(
                "user", user,
                "recentRecords", recentRecords,
                "nextLevel", Map.of(
                    "currentLevel", currentLevel,
                    "nextLevel", currentLevel + 1,
                    "pointsNeeded", pointsNeeded,
                    "progress", Math.min(100, (currentPoints * 100) / nextLevelPoints)
                )
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", dashboard
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 다음 레벨까지 필요한 포인트 계산
     */
    private int calculateNextLevelPoints(int currentLevel) {
        // 간단한 레벨업 포인트 계산 (실제로는 더 복잡한 로직 가능)
        return (currentLevel + 1) * 100;
    }
}
