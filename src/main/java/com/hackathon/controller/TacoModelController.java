package com.hackathon.controller;

import com.hackathon.service.TacoModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/taco")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class TacoModelController {

    @Autowired
    private TacoModelService tacoModelService;

    @PostMapping("/detect")
    public ResponseEntity<?> detectWaste(@RequestParam("image") MultipartFile image) {
        try {
            Map<String, Object> result = tacoModelService.detectWaste(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", "쓰레기 탐지 중 오류가 발생했습니다: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "status", 500
                ));
        }
    }

    @PostMapping("/status")
    public ResponseEntity<?> getModelStatus() {
        try {
            Map<String, Object> status = tacoModelService.getModelStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", "모델 상태 확인 중 오류가 발생했습니다: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "status", 500
                ));
        }
    }
}
