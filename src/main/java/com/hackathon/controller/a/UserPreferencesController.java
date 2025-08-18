package com.hackathon.controller.a;

import com.hackathon.dto.a.UserPreferencesDto;
import com.hackathon.service.a.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-preferences")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserPreferencesController {
    
    private final UserPreferencesService userPreferencesService;
    
    // ===== 사용자 선호도 =====
    
    /**
     * 모든 사용자 선호도 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUserPreferences() {
        try {
            List<UserPreferencesDto> preferences = userPreferencesService.getAllUserPreferences();
            
            List<UserPreferencesDto> preferenceDtos = preferences.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", preferenceDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 선호도 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserPreferenceById(@PathVariable Long id) {
        try {
            UserPreferencesDto preference = userPreferencesService.getUserPreferenceById(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", preference
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 선호도 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPreferencesByUserId(@PathVariable Long userId) {
        try {
            List<UserPreferencesDto> preferences = userPreferencesService.getUserPreferencesByUserId(userId);
            
            List<UserPreferencesDto> preferenceDtos = preferences.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", preferenceDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 특정 타입 선호도 조회
     */
    @GetMapping("/user/{userId}/type/{preferenceType}")
    public ResponseEntity<Map<String, Object>> getUserPreferenceByUserIdAndType(
            @PathVariable Long userId, 
            @PathVariable String preferenceType) {
        try {
            Optional<UserPreferencesDto> preference = userPreferencesService.getUserPreferenceByUserIdAndType(userId, preferenceType);
            
            if (preference.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", preference.get()
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "해당 타입의 사용자 선호도를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 선호도 타입별 조회
     */
    @GetMapping("/type/{preferenceType}")
    public ResponseEntity<Map<String, Object>> getUserPreferencesByType(@PathVariable String preferenceType) {
        try {
            List<UserPreferencesDto> preferences = userPreferencesService.getUserPreferencesByType(preferenceType);
            
            List<UserPreferencesDto> preferenceDtos = preferences.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", preferenceDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 새로운 사용자 선호도 생성
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUserPreference(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String preferenceType = request.get("preferenceType").toString();
            String preferenceValue = request.get("preferenceValue").toString();
            
            UserPreferencesDto preference = userPreferencesService.createUserPreference(userId, preferenceType, preferenceValue);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자 선호도가 성공적으로 생성되었습니다.",
                "data", preference
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 선호도 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUserPreference(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String preferenceValue = request.get("preferenceValue").toString();
            
            UserPreferencesDto preference = userPreferencesService.updateUserPreference(id, preferenceValue);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자 선호도가 성공적으로 수정되었습니다.",
                "data", preference
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자 선호도 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserPreference(@PathVariable Long id) {
        try {
            userPreferencesService.deleteUserPreference(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자 선호도가 성공적으로 삭제되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private UserPreferencesDto convertToDto(UserPreferencesDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }
}
