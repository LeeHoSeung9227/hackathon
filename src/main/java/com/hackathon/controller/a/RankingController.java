package com.hackathon.controller.a;

import com.hackathon.service.a.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;
    
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
        log.info("=== RankingController 생성자 실행 ===");
        log.info("rankingService: {}", rankingService != null ? "주입됨" : "주입실패");
        log.info("=== RankingController 생성자 완료 ===");
    }

    /**
     * 전체 랭킹 조회 (개인 + 단과대)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRankings() {
        try {
            log.info("=== 전체 랭킹 조회 시작 ===");
            Map<String, Object> rankings = rankingService.getAllRankings();
            log.info("=== 전체 랭킹 조회 완료 ===");
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", rankings
            ));
        } catch (Exception e) {
            log.error("전체 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 상위 랭킹 조회
     */
    @GetMapping("/top")
    public ResponseEntity<Map<String, Object>> getTopRankings(
            @RequestParam(defaultValue = "TOTAL") String scopeType,
            @RequestParam(defaultValue = "50") int limit) {
        try {
            log.info("=== 상위 랭킹 조회 시작 ===");
            log.info("스코프 타입: {}, 제한: {}", scopeType, limit);
            List<Map<String, Object>> rankings = rankingService.getTopRankings(scopeType, limit);
            log.info("=== 상위 랭킹 조회 완료 ===");
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", rankings
            ));
        } catch (Exception e) {
            log.error("상위 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 단과대 랭킹 조회 (스코프별)
     */
    @GetMapping("/college")
    public ResponseEntity<Map<String, Object>> getCollegeRankings(
            @RequestParam(defaultValue = "TOTAL") String scopeType) {
        try {
            log.info("=== 단과대 랭킹 조회 시작 (Controller) ===");
            log.info("스코프 타입: {}", scopeType);
            
            List<Map<String, Object>> rankings = rankingService.getCollegeRankings(scopeType);
            
            log.info("=== 단과대 랭킹 조회 완료 (Controller) ===");
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", rankings
            ));
        } catch (Exception e) {
            log.error("단과대 랭킹 조회 중 에러 발생 (Controller): {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
