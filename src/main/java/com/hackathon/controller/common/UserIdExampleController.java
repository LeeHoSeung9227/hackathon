package com.hackathon.controller.common;

import com.hackathon.util.UserIdParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * userId를 Object로 받아서 파싱하는 예시 컨트롤러
 * 프론트엔드에서 어떤 타입으로 보내도 자동으로 Long으로 변환
 */
@RestController
@RequestMapping("/api/example")
public class UserIdExampleController {
    
    private static final Logger log = LoggerFactory.getLogger(UserIdExampleController.class);

    /**
     * GET 요청에서 userId를 Object로 받기
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Object userIdObj) {
        try {
            // Object를 Long으로 파싱
            Long userId = UserIdParser.parseUserId(userIdObj);
            
            log.info("✅ userId 파싱 성공: {} -> {}", userIdObj, userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "userId 파싱 성공",
                "originalValue", userIdObj,
                "parsedValue", userId,
                "originalType", userIdObj.getClass().getSimpleName()
            ));
            
        } catch (IllegalArgumentException e) {
            log.error("❌ userId 파싱 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "originalValue", userIdObj,
                "originalType", userIdObj.getClass().getSimpleName()
            ));
        }
    }

    /**
     * POST 요청에서 userId를 Object로 받기 (FormData)
     */
    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam Object userIdObj) {
        try {
            // Object를 Long으로 파싱
            Long userId = UserIdParser.parseUserId(userIdObj);
            
            log.info("✅ userId 파싱 성공: {} -> {}", userIdObj, userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "userId 파싱 성공",
                "originalValue", userIdObj,
                "parsedValue", userId,
                "originalType", userIdObj.getClass().getSimpleName()
            ));
            
        } catch (IllegalArgumentException e) {
            log.error("❌ userId 파싱 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "originalValue", userIdObj,
                "originalType", userIdObj.getClass().getSimpleName()
            ));
        }
    }

    /**
     * POST 요청에서 userId를 Object로 받기 (JSON)
     */
    @PostMapping("/user/json")
    public ResponseEntity<Map<String, Object>> createUserJson(@RequestBody Map<String, Object> request) {
        try {
            Object userIdObj = request.get("userId");
            
            if (userIdObj == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "userId가 요청에 포함되지 않았습니다"
                ));
            }
            
            // Object를 Long으로 파싱
            Long userId = UserIdParser.parseUserId(userIdObj);
            
            log.info("✅ userId 파싱 성공: {} -> {}", userIdObj, userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "userId 파싱 성공",
                "originalValue", userIdObj,
                "parsedValue", userId,
                "originalType", userIdObj.getClass().getSimpleName()
            ));
            
        } catch (IllegalArgumentException e) {
            log.error("❌ userId 파싱 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    /**
     * userId 유효성 검사
     */
    @GetMapping("/validate/{userId}")
    public ResponseEntity<Map<String, Object>> validateUserId(@PathVariable Object userIdObj) {
        boolean isValid = UserIdParser.isValidUserId(userIdObj);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "isValid", isValid,
            "originalValue", userIdObj,
            "originalType", userIdObj.getClass().getSimpleName(),
            "message", isValid ? "유효한 userId입니다" : "유효하지 않은 userId입니다"
        ));
    }
}
