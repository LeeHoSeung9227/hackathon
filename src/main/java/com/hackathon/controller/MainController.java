package com.hackathon.controller;

import com.hackathon.dto.UserDto;
import com.hackathon.dto.WasteRecordDto;
import com.hackathon.service.UserService;
import com.hackathon.service.WasteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MainController {

    private final UserService userService;
    private final WasteRecordService wasteRecordService;

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Map<String, Object>> getDashboard(@PathVariable Long userId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // 사용자 정보
        UserDto user = userService.getUserById(userId);
        dashboard.put("user", user);
        
        // 최근 쓰레기 분리 내역 (최근 3개)
        List<WasteRecordDto> recentRecords = wasteRecordService.getRecentRecordsByUserId(userId, 3);
        dashboard.put("recentRecords", recentRecords);
        
        // 다음 등급까지 필요한 포인트
        int nextLevelPoints = calculateNextLevelPoints(user.getPoints());
        dashboard.put("nextLevelPoints", nextLevelPoints);
        
        return ResponseEntity.ok(dashboard);
    }
    
    private int calculateNextLevelPoints(int currentPoints) {
        if (currentPoints < 100) return 100 - currentPoints;
        if (currentPoints < 500) return 500 - currentPoints;
        if (currentPoints < 1000) return 1000 - currentPoints;
        if (currentPoints < 2000) return 2000 - currentPoints;
        return 0; // 최고 등급
    }
}

