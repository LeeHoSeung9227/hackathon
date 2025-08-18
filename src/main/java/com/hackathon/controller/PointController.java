package com.hackathon.controller;

import com.hackathon.dto.PointHistoryDto;
import com.hackathon.dto.ExchangeHistoryDto;
import com.hackathon.service.PointHistoryService;
import com.hackathon.service.ExchangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PointController {
    
    private final PointHistoryService pointHistoryService;
    private final ExchangeHistoryService exchangeHistoryService;
    
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
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByImagesId(imagesId);
            
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
     * 변경 타입별 포인트 내역 조회
     */
    @GetMapping("/user/{userId}/type/{changeType}")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByType(
            @PathVariable Long userId, 
            @PathVariable String changeType) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserIdAndChangeType(userId, changeType);
            
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
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserIdAndDateRange(userId, startDate, endDate);
            
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

    // ===== 교환 내역 =====
    
    /**
     * 사용자 교환 내역 조회
     */
    @GetMapping("/user/{userId}/exchanges")
    public ResponseEntity<Map<String, Object>> getUserExchangeHistory(@PathVariable Long userId) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByUserId(userId);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToExchangeDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", exchangeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 상품별 교환 내역 조회
     */
    @GetMapping("/exchanges/product/{productId}")
    public ResponseEntity<Map<String, Object>> getProductExchangeHistory(@PathVariable Long productId) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByProductId(productId);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToExchangeDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", exchangeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 상품별 교환 내역 조회
     */
    @GetMapping("/user/{userId}/exchanges/product/{productId}")
    public ResponseEntity<Map<String, Object>> getUserProductExchangeHistory(
            @PathVariable Long userId, 
            @PathVariable Long productId) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByUserIdAndProductId(userId, productId);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToExchangeDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", exchangeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 교환 내역 조회
     */
    @GetMapping("/user/{userId}/exchanges/range")
    public ResponseEntity<Map<String, Object>> getUserExchangeHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByUserIdAndDateRange(userId, startDate, endDate);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToExchangeDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", exchangeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }
    
    private ExchangeHistoryDto convertToExchangeDto(ExchangeHistoryDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }
}
