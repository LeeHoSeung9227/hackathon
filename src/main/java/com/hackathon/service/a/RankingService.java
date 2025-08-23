package com.hackathon.service.a;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RankingService {
    
    /**
     * 사용자별 랭킹 조회 (스코프별)
     */
    public List<Map<String, Object>> getUserRankings(Long userId, String scopeType) {
        try {
            // 간단한 테스트용 랭킹 반환 (시간별 포인트 계산)
            List<Map<String, Object>> rankings = new ArrayList<>();
            
            Map<String, Object> ranking = new HashMap<>();
            ranking.put("userId", userId);
            ranking.put("username", "testuser");
            ranking.put("name", "테스트 사용자");
            ranking.put("college", "공과대학");
            ranking.put("campus", "서울캠퍼스");
            ranking.put("points", calculateScopePoints(scopeType));
            ranking.put("scopeType", scopeType);
            ranking.put("rank", 1);
            
            rankings.add(ranking);
            return rankings;
                
        } catch (Exception e) {
            throw new RuntimeException("랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 사용자 랭킹 요약 (전체 스코프)
     */
    public Map<String, Object> getUserRankingSummary(Long userId) {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // 각 스코프별 랭킹 조회
            String[] scopeTypes = {"DAILY", "WEEKLY", "MONTHLY", "TOTAL"};
            
            for (String scopeType : scopeTypes) {
                List<Map<String, Object>> rankings = getUserRankings(userId, scopeType);
                if (!rankings.isEmpty()) {
                    Map<String, Object> ranking = rankings.get(0);
                    summary.put(scopeType.toLowerCase(), Map.of(
                        "rank", ranking.get("rank"),
                        "points", ranking.get("points"),
                        "totalUsers", getTotalUsersInScope(scopeType)
                    ));
                }
            }
            
            return summary;
            
        } catch (Exception e) {
            throw new RuntimeException("랭킹 요약 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 전체 랭킹 조회 (개인 + 단과대)
     */
    public Map<String, Object> getAllRankings() {
        try {
            Map<String, Object> allRankings = new HashMap<>();
            
            // 개인 랭킹 (전체)
            List<Map<String, Object>> individualRankings = getTopRankings("TOTAL", 50);
            allRankings.put("individual", individualRankings);
            
            // 단과대 랭킹 (전체)
            List<Map<String, Object>> collegeRankings = getCollegeRankings("TOTAL");
            allRankings.put("college", collegeRankings);
            
            return allRankings;
            
        } catch (Exception e) {
            throw new RuntimeException("전체 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 상위 랭킹 조회
     */
    public List<Map<String, Object>> getTopRankings(String scopeType, int limit) {
        try {
            List<Map<String, Object>> rankings = new ArrayList<>();
            
            for (int i = 1; i <= Math.min(limit, 10); i++) {
                Map<String, Object> ranking = new HashMap<>();
                ranking.put("userId", i);
                ranking.put("username", "user" + i);
                ranking.put("name", "사용자" + i);
                ranking.put("college", "공과대학");
                ranking.put("campus", "서울캠퍼스");
                ranking.put("points", calculateScopePoints(scopeType) * i);
                ranking.put("scopeType", scopeType);
                ranking.put("rank", i);
                rankings.add(ranking);
            }
            
            return rankings;
            
        } catch (Exception e) {
            throw new RuntimeException("상위 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 단과대 랭킹 조회 (스코프별)
     */
    public List<Map<String, Object>> getCollegeRankings(String scopeType) {
        try {
            log.info("=== 단과대 랭킹 조회 시작 ===");
            log.info("스코프 타입: {}", scopeType);
            
            List<Map<String, Object>> collegeRankings = new ArrayList<>();
            
            String[] colleges = {"공과대학", "인문대학", "자연대학"};
            log.info("처리할 단과대: {}", Arrays.toString(colleges));
            
            for (int i = 0; i < colleges.length; i++) {
                log.info("단과대 {} 처리 중...", colleges[i]);
                
                Map<String, Object> collegeRanking = new HashMap<>();
                collegeRanking.put("college", colleges[i]);
                collegeRanking.put("totalPoints", calculateScopePoints(scopeType) * (1000 + (i * 500)));
                collegeRanking.put("userCount", 10 + (i * 5));
                collegeRanking.put("scopeType", scopeType);
                collegeRanking.put("rank", i + 1);
                collegeRankings.add(collegeRanking);
                
                log.info("단과대 {} 처리 완료: {}", colleges[i], collegeRanking);
            }
            
            log.info("=== 단과대 랭킹 조회 완료 ===");
            return collegeRankings;
            
        } catch (Exception e) {
            log.error("단과대 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("단과대 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 스코프별 포인트 계산 (테스트용)
     */
    private int calculateScopePoints(String scopeType) {
        switch (scopeType.toUpperCase()) {
            case "DAILY":
                return 50;      // 일간: 50점
            case "WEEKLY":
                return 200;     // 주간: 200점
            case "MONTHLY":
                return 800;     // 월간: 800점
            case "TOTAL":
                return 1000;    // 전체: 1000점
            default:
                return 100;
        }
    }
    
    /**
     * 스코프별 총 사용자 수
     */
    private int getTotalUsersInScope(String scopeType) {
        // 실제로는 해당 기간에 활동한 사용자 수를 계산해야 함
        // 임시로 전체 사용자 수 반환
        return 10;
    }
}
