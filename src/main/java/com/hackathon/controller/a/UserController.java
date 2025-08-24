package com.hackathon.controller.a;

import com.hackathon.dto.a.UserDto;
import com.hackathon.service.a.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hackathon.entity.a.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    
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
    
    // 랭킹 관련 메서드들은 임시로 제거됨
    
    /**
     * 테스트용 간단한 API
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        try {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "UserController 정상 작동",
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * 닉네임, 학교명, 단과대로 사용자 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUser(
            @RequestParam String nickname,
            @RequestParam String school,
            @RequestParam String college) {
        log.info("=== GET /api/users/search 엔드포인트 호출됨 ===");
        log.info("검색 조건: nickname={}, school={}, college={}", nickname, school, college);
        
        try {
            User user = userService.searchUserByNicknameAndSchoolAndCollege(nickname, school, college);
            
            if (user != null) {
                log.info("사용자 검색 성공: userId={}", user.getId());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", user);
                return ResponseEntity.ok(response);
            } else {
                log.info("사용자를 찾을 수 없음");
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("사용자 검색 중 오류 발생", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "사용자 검색 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
