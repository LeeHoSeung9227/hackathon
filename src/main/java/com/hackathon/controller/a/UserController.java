package com.hackathon.controller.a;

import com.hackathon.dto.a.UserDto;
import com.hackathon.service.a.UserService;
import com.hackathon.service.a.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.hackathon.dto.a.RankingDto;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final RankingService rankingService;
    
    /**
     * 모든 사용자 목록 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 특정 사용자 정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", user
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 새 사용자 생성
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String name = request.get("name");
            String college = request.get("college");
            String campus = request.get("campus");
            
            UserDto user = userService.createUser(username, email, password, name, college, campus);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", user
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자가 삭제되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 랭킹 조회 (스코프별)
     */
    @GetMapping("/{userId}/rankings/scope/{scopeType}")
    public ResponseEntity<Map<String, Object>> getUserRankingsByScope(
            @PathVariable Long userId, 
            @PathVariable String scopeType) {
        try {
            List<Map<String, Object>> rankings = rankingService.getUserRankings(userId, scopeType);
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
     * 사용자 랭킹 요약
     */
    @GetMapping("/{userId}/rankings")
    public ResponseEntity<Map<String, Object>> getUserRankingSummary(@PathVariable Long userId) {
        try {
            Map<String, Object> summary = rankingService.getUserRankingSummary(userId);
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
