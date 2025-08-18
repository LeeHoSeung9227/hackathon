package com.hackathon.controller;

import com.hackathon.dto.UserDto;
import com.hackathon.dto.RankingDto;
import com.hackathon.service.UserService;
import com.hackathon.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final RankingService rankingService;

    // ===== 사용자 관리 =====
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // ===== 랭킹 시스템 =====
    
    /**
     * 특정 범위의 랭킹 조회
     */
    @GetMapping("/{id}/rankings/scope/{scopeType}")
    public ResponseEntity<Map<String, Object>> getUserRankingsByScope(
            @PathVariable Long id, 
            @PathVariable String scopeType) {
        try {
            List<RankingDto> rankings = rankingService.getRankingsByCategory(scopeType);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", rankings
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 개별 랭킹 조회
     */
    @GetMapping("/{id}/rankings")
    public ResponseEntity<Map<String, Object>> getUserRankings(@PathVariable Long id) {
        try {
            RankingDto ranking = rankingService.getUserRank(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", ranking
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 랭킹 정보 요약
     */
    @GetMapping("/{id}/ranking-summary")
    public ResponseEntity<Map<String, Object>> getUserRankingSummary(@PathVariable Long id) {
        try {
            RankingDto ranking = rankingService.getUserRank(id);
            Map<String, Object> summary = Map.of(
                "rank", ranking.getRankPosition(),
                "points", ranking.getPoints(),
                "membershipLevel", ranking.getMembershipLevel(),
                "category", "INDIVIDUAL"
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
}
