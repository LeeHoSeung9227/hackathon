package com.hackathon.controller.a;

import com.hackathon.dto.a.PointHistoryDto;
import com.hackathon.service.a.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    
    private final PointHistoryService pointHistoryService;
    
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
        return ResponseEntity.badRequest().body(Map.of("error", "이미지별 포인트 내역 조회는 지원하지 않습니다."));
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
     * 날짜 범위별 포인트 내역 조회
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.badRequest().body(Map.of("error", "날짜 범위별 포인트 내역 조회는 지원하지 않습니다."));
    }

    // ===== DTO 변환 메서드 =====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }
}
