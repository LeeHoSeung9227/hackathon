package com.hackathon.controller;

import com.hackathon.dto.WasteRecordDto;
import com.hackathon.service.WasteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/camera")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CameraController {

    private final WasteRecordService wasteRecordService;

    @PostMapping("/recognize")
    public ResponseEntity<Map<String, Object>> recognizeWaste(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new java.util.HashMap<>();
        
        // 실제로는 AI 모델을 사용하여 쓰레기 인식
        // 여기서는 간단한 시뮬레이션
        String wasteType = (String) request.get("wasteType");
        Long userId = Long.valueOf(request.get("userId").toString());
        
        // 포인트 계산 (쓰레기 타입별로 다름)
        int earnedPoints = calculatePoints(wasteType);
        
        // 성공 응답
        response.put("success", true);
        response.put("wasteType", wasteType);
        response.put("earnedPoints", earnedPoints);
        response.put("message", "Today you saved a polar bear!");
        
        return ResponseEntity.ok(response);
    }
    
    private int calculatePoints(String wasteType) {
        switch (wasteType.toUpperCase()) {
            case "PET": return 10;
            case "CAN": return 15;
            case "PAPER": return 5;
            case "GLASS": return 20;
            default: return 5;
        }
    }
}

