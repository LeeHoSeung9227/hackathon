package com.hackathon.controller.b;

import com.hackathon.dto.b.ExchangeHistoryDto;
import com.hackathon.service.b.ExchangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExchangeController {
    
    private final ExchangeHistoryService exchangeHistoryService;
    
    // ===== 교환 내역 =====
    
    /**
     * 모든 교환 내역 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExchangeHistory() {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getAllExchangeHistory();
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToDto)
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
     * 교환 내역 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getExchangeHistoryById(@PathVariable Long id) {
        try {
            ExchangeHistoryDto exchange = exchangeHistoryService.getExchangeHistoryById(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", exchange
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 교환 내역 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserExchangeHistory(@PathVariable Long userId) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByUserId(userId);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToDto)
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
    @GetMapping("/product/{productName}")
    public ResponseEntity<Map<String, Object>> getProductExchangeHistory(@PathVariable String productName) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByProductName(productName);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToDto)
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
    @GetMapping("/user/{userId}/product/{productName}")
    public ResponseEntity<Map<String, Object>> getUserProductExchangeHistory(
            @PathVariable Long userId, 
            @PathVariable String productName) {
        try {
            List<ExchangeHistoryDto> exchanges = exchangeHistoryService.getExchangeHistoryByUserIdAndProductName(userId, productName);
            
            List<ExchangeHistoryDto> exchangeDtos = exchanges.stream()
                .map(this::convertToDto)
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
     * 새로운 교환 내역 생성
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createExchangeHistory(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String productName = request.get("productName").toString();
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            BigDecimal totalAmount = new BigDecimal(request.get("totalAmount").toString());
            
            ExchangeHistoryDto exchange = exchangeHistoryService.createExchangeHistory(userId, productName, quantity, totalAmount);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "교환 내역이 성공적으로 생성되었습니다.",
                "data", exchange
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 교환 내역 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateExchangeHistory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String productName = request.get("productName").toString();
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            BigDecimal totalAmount = new BigDecimal(request.get("totalAmount").toString());
            
            ExchangeHistoryDto exchange = exchangeHistoryService.updateExchangeHistory(id, productName, quantity, totalAmount);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "교환 내역이 성공적으로 수정되었습니다.",
                "data", exchange
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 교환 내역 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteExchangeHistory(@PathVariable Long id) {
        try {
            exchangeHistoryService.deleteExchangeHistory(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "교환 내역이 성공적으로 삭제되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private ExchangeHistoryDto convertToDto(ExchangeHistoryDto dto) {
        return dto; // 이미 DTO이므로 그대로 반환
    }
}
