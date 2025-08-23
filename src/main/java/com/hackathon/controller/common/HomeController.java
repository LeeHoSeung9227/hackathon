package com.hackathon.controller.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    /**
     * 홈 페이지
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "환경보호 쓰레기 분리 포인트 시스템에 오신 것을 환영합니다!",
            "version", "1.0.0",
            "status", "running"
        ));
    }

    /**
     * 헬스 체크
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "status", "healthy",
            "timestamp", System.currentTimeMillis(),
            "service", "hackathon-backend"
        ));
    }
}
