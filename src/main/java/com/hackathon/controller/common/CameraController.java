package com.hackathon.controller.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

    /**
     * 쓰레기 인식
     */
    @PostMapping("/recognize")
    public ResponseEntity<Map<String, Object>> recognizeWaste(@RequestBody Map<String, String> request) {
        try {
            String wasteType = request.get("wasteType");
            
            if (wasteType == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "쓰레기 타입은 필수입니다."));
            }
            
            // 포인트 계산 로직
            int earnedPoints = calculatePoints(wasteType);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "쓰레기 인식이 완료되었습니다.",
                "data", Map.of(
                    "wasteType", wasteType,
                    "earnedPoints", earnedPoints,
                    "message", getWasteMessage(wasteType)
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 포인트 계산
     */
    private int calculatePoints(String wasteType) {
        return switch (wasteType.toUpperCase()) {
            case "PLASTIC" -> 10;
            case "PAPER" -> 8;
            case "GLASS" -> 12;
            case "METAL" -> 15;
            case "FOOD_WASTE" -> 5;
            default -> 3;
        };
    }
    
    /**
     * 쓰레기 타입별 메시지
     */
    private String getWasteMessage(String wasteType) {
        return switch (wasteType.toUpperCase()) {
            case "PLASTIC" -> "플라스틱 분리수거 완료! 환경보호에 기여했습니다.";
            case "PAPER" -> "종이 분리수거 완료! 자원 재활용에 도움이 됩니다.";
            case "GLASS" -> "유리 분리수거 완료! 안전한 재활용을 위해 노력했습니다.";
            case "METAL" -> "금속 분리수거 완료! 귀중한 자원을 보호했습니다.";
            case "FOOD_WASTE" -> "음식물 쓰레기 분리수거 완료! 퇴비화에 기여했습니다.";
            default -> "쓰레기 분리수거 완료! 환경보호에 참여했습니다.";
        };
    }
}
