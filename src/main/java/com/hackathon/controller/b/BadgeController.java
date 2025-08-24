package com.hackathon.controller.b;

import com.hackathon.entity.b.Badge;
import com.hackathon.entity.b.UserBadge;
import com.hackathon.service.b.BadgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    private static final Logger log = LoggerFactory.getLogger(BadgeController.class);
    private final BadgeService badgeService;
    
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }
    
    // 모든 뱃지 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBadges() {
        try {
            List<Badge> badges = badgeService.getAllBadges();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", badges);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("뱃지 조회 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "뱃지 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 사용자별 뱃지 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBadges(@PathVariable Long userId) {
        try {
            List<UserBadge> userBadges = badgeService.getUserBadges(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userBadges);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("사용자 뱃지 조회 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "사용자 뱃지 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 뱃지 획득
    @PostMapping("/earn")
    public ResponseEntity<Map<String, Object>> earnBadge(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long badgeId = Long.valueOf(request.get("badgeId").toString());
            
            badgeService.earnBadge(userId, badgeId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "뱃지를 획득했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("뱃지 획득 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "뱃지 획득 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // AI 분석 완료 후 뱃지 조건 확인
    @PostMapping("/check-conditions")
    public ResponseEntity<Map<String, Object>> checkBadgeConditions(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            
            badgeService.checkBadgeConditionsAfterAnalysis(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "뱃지 조건을 확인했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("뱃지 조건 확인 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "뱃지 조건 확인 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 모든 뱃지 조건 체크
    @PostMapping("/check-all-conditions")
    public ResponseEntity<Map<String, Object>> checkAllBadgeConditions(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            
            badgeService.checkAllBadgeConditions(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "모든 뱃지 조건을 확인했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("모든 뱃지 조건 확인 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "모든 뱃지 조건 확인 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 기본 뱃지 초기화
    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializeDefaultBadges() {
        try {
            badgeService.initializeDefaultBadges();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "기본 뱃지가 초기화되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("기본 뱃지 초기화 중 오류 발생: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "기본 뱃지 초기화 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
