package com.hackathon.controller.a;

import com.hackathon.dto.a.UserDto;
import com.hackathon.service.a.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        return ResponseEntity.ok(
            userService.createUser(
                userDto.getUsername(),
                userDto.getEmail(),
                "tempPassword",
                userDto.getName(),
                userDto.getCollege(),
                userDto.getCampus()
            )
        );
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

    // ===== 랭킹 시스템 (임시 비활성화) =====
    
    /**
     * 특정 범위의 랭킹 조회
     */
    @GetMapping("/{id}/rankings/scope/{scopeType}")
    public ResponseEntity<Map<String, Object>> getUserRankingsByScope(
            @PathVariable Long id, 
            @PathVariable String scopeType) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "랭킹 시스템이 임시로 비활성화되었습니다."));
    }
    
    /**
     * 사용자 개별 랭킹 조회
     */
    @GetMapping("/{id}/rankings")
    public ResponseEntity<Map<String, Object>> getUserRankings(@PathVariable Long id) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "랭킹 시스템이 임시로 비활성화되었습니다."));
    }
    
    /**
     * 사용자 랭킹 정보 요약
     */
    @GetMapping("/{id}/rankings/summary")
    public ResponseEntity<Map<String, Object>> getUserRankingSummary(@PathVariable Long id) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "랭킹 시스템이 임시로 비활성화되었습니다."));
    }
}
