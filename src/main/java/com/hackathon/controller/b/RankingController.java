package com.hackathon.controller.b;

import com.hackathon.service.b.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rankings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class RankingController {

    private final RankingService rankingService;

    /** 개인 랭킹 Top 30/100 (TOTAL 고정) */
    @GetMapping("/individual/top")
    public ResponseEntity<Map<String, Object>> getIndividualTop(@RequestParam(defaultValue = "30") int limit) {
        try {
            validateLimit(limit);
            List<Map<String, Object>> rankings = rankingService.getTopIndividuals(limit);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "scopeType", "TOTAL",
                            "limit", limit,
                            "rankings", rankings
                    )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "개인 랭킹 조회 중 오류가 발생했습니다."));
        }
    }

    /** 단과대 랭킹 Top 30/100 (TOTAL 고정: 단과대 합계) */
    @GetMapping("/college/top")
    public ResponseEntity<Map<String, Object>> getCollegeTop(@RequestParam(defaultValue = "30") int limit) {
        try {
            validateLimit(limit);
            List<Map<String, Object>> rankings = rankingService.getTopColleges(limit);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "scopeType", "TOTAL",
                            "limit", limit,
                            "rankings", rankings
                    )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("단과대 랭킹 조회 오류", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "단과대 랭킹 조회 중 오류가 발생했습니다."));
        }
    }

    /** 캠퍼스(학교) 랭킹 Top 30/100 (TOTAL 고정: 캠퍼스 합계) */
    @GetMapping("/campus/top")
    public ResponseEntity<Map<String, Object>> getCampusTop(@RequestParam(defaultValue = "30") int limit) {
        try {
            validateLimit(limit);
            List<Map<String, Object>> rankings = rankingService.getTopCampuses(limit);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "scopeType", "TOTAL",
                            "limit", limit,
                            "rankings", rankings
                    )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("캠퍼스 랭킹 조회 오류", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "캠퍼스 랭킹 조회 중 오류가 발생했습니다."));
        }
    }

    /* ===== 공통 ===== */
    private void validateLimit(int limit) {
        if (limit != 30 && limit != 100) {
            throw new IllegalArgumentException("limit은 30 또는 100만 허용됩니다.");
        }
    }
}
