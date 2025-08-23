package com.hackathon.service.a;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.repository.a.UserRepository;
import com.hackathon.entity.a.PointHistory;
import com.hackathon.entity.a.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * 사용자별 랭킹 조회 (스코프별)
     */
    public List<Map<String, Object>> getUserRankings(Long userId, String scopeType) {
        try {
            log.info("=== 사용자 랭킹 조회 시작 ===");
            log.info("사용자 ID: {}, 스코프: {}", userId, scopeType);
            
            // 실제 사용자 정보 조회
            var user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
            }
            
            // 스코프별 포인트 계산
            int scopePoints = calculateUserScopePoints(userId, scopeType);
            
            Map<String, Object> ranking = new HashMap<>();
            ranking.put("userId", userId);
            ranking.put("username", user.get().getUsername());
            ranking.put("name", user.get().getName());
            ranking.put("college", user.get().getCollege());
            ranking.put("campus", user.get().getCampus());
            ranking.put("points", scopePoints);
            ranking.put("scopeType", scopeType);
            ranking.put("rank", calculateUserRank(userId, scopeType));
            
            log.info("=== 사용자 랭킹 조회 완료 ===");
            return List.of(ranking);
                
        } catch (Exception e) {
            log.error("사용자 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 사용자 랭킹 요약 (전체 스코프)
     */
    public Map<String, Object> getUserRankingSummary(Long userId) {
        try {
            log.info("=== 사용자 랭킹 요약 시작 ===");
            log.info("사용자 ID: {}", userId);
            
            Map<String, Object> summary = new HashMap<>();
            
            // 각 스코프별 랭킹 조회
            String[] scopeTypes = {"DAILY", "WEEKLY", "MONTHLY", "TOTAL"};
            
            for (String scopeType : scopeTypes) {
                int points = calculateUserScopePoints(userId, scopeType);
                int rank = calculateUserRank(userId, scopeType);
                int totalUsers = getTotalUsersInScope(scopeType);
                
                summary.put(scopeType.toLowerCase(), Map.of(
                    "rank", rank,
                    "points", points,
                    "totalUsers", totalUsers
                ));
            }
            
            log.info("=== 사용자 랭킹 요약 완료 ===");
            return summary;
            
        } catch (Exception e) {
            log.error("사용자 랭킹 요약 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("랭킹 요약 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 전체 랭킹 조회 (개인 + 단과대)
     */
    public Map<String, Object> getAllRankings() {
        try {
            log.info("=== 전체 랭킹 조회 시작 ===");
            
            Map<String, Object> allRankings = new HashMap<>();
            
            // 개인 랭킹 (전체)
            List<Map<String, Object>> individualRankings = getTopRankings("TOTAL", 50);
            allRankings.put("individual", individualRankings);
            
            // 단과대 랭킹 (전체)
            List<Map<String, Object>> collegeRankings = getCollegeRankings("TOTAL");
            allRankings.put("college", collegeRankings);
            
            log.info("=== 전체 랭킹 조회 완료 ===");
            return allRankings;
            
        } catch (Exception e) {
            log.error("전체 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("전체 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 상위 랭킹 조회 (실제 데이터 사용)
     */
    public List<Map<String, Object>> getTopRankings(String scopeType, int limit) {
        try {
            log.info("=== 상위 랭킹 조회 시작 ===");
            log.info("스코프: {}, 제한: {}", scopeType, limit);
            
            // 모든 사용자 조회
            List<User> allUsers = userRepository.findAll();
            
            // 스코프별 포인트 계산 및 정렬
            List<Map<String, Object>> rankings = allUsers.stream()
                .map(user -> {
                    Map<String, Object> ranking = new HashMap<>();
                    ranking.put("userId", user.getId());
                    ranking.put("username", user.getUsername());
                    ranking.put("name", user.getName());
                    ranking.put("college", user.getCollege());
                    ranking.put("campus", user.getCampus());
                    ranking.put("points", calculateUserScopePoints(user.getId(), scopeType));
                    ranking.put("scopeType", scopeType);
                    return ranking;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("points"), (Integer) a.get("points")))
                .limit(limit)
                .collect(Collectors.toList());
            
            // 순위 추가
            for (int i = 0; i < rankings.size(); i++) {
                rankings.get(i).put("rank", i + 1);
            }
            
            log.info("=== 상위 랭킹 조회 완료 ===");
            return rankings;
            
        } catch (Exception e) {
            log.error("상위 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("상위 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 단과대 랭킹 조회 (실제 데이터 사용)
     */
    public List<Map<String, Object>> getCollegeRankings(String scopeType) {
        try {
            log.info("=== 단과대 랭킹 조회 시작 ===");
            log.info("스코프 타입: {}", scopeType);
            
            // 단과대별 사용자 그룹화
            Map<String, List<User>> collegeGroups = userRepository.findAll().stream()
                .collect(Collectors.groupingBy(User::getCollege));
            
            List<Map<String, Object>> collegeRankings = new ArrayList<>();
            
            for (Map.Entry<String, List<User>> entry : collegeGroups.entrySet()) {
                String college = entry.getKey();
                List<User> users = entry.getValue();
                
                // 단과대 총 포인트 계산
                int totalPoints = users.stream()
                    .mapToInt(user -> calculateUserScopePoints(user.getId(), scopeType))
                    .sum();
                
                Map<String, Object> collegeRanking = new HashMap<>();
                collegeRanking.put("college", college);
                collegeRanking.put("totalPoints", totalPoints);
                collegeRanking.put("userCount", users.size());
                collegeRanking.put("scopeType", scopeType);
                collegeRankings.add(collegeRanking);
            }
            
            // 포인트 순으로 정렬
            collegeRankings.sort((a, b) -> Integer.compare((Integer) b.get("totalPoints"), (Integer) a.get("totalPoints")));
            
            // 순위 추가
            for (int i = 0; i < collegeRankings.size(); i++) {
                collegeRankings.get(i).put("rank", i + 1);
            }
            
            log.info("=== 단과대 랭킹 조회 완료 ===");
            return collegeRankings;
            
        } catch (Exception e) {
            log.error("단과대 랭킹 조회 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("단과대 랭킹 조회에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 사용자별 스코프 포인트 계산 (실제 데이터 사용)
     */
    private int calculateUserScopePoints(Long userId, String scopeType) {
        try {
            LocalDateTime startTime = getScopeStartTime(scopeType);
            LocalDateTime endTime = LocalDateTime.now();
            
            // 해당 기간의 포인트 히스토리 조회
            List<PointHistory> histories = pointHistoryRepository.findByUserIdAndCreatedAtBetween(userId, startTime, endTime);
            
            // 포인트 합계 계산
            return histories.stream()
                .mapToInt(PointHistory::getPoints)
                .sum();
                
        } catch (Exception e) {
            log.warn("사용자 {} 스코프 {} 포인트 계산 실패: {}", userId, scopeType, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 사용자 순위 계산 (실제 데이터 사용)
     */
    private int calculateUserRank(Long userId, String scopeType) {
        try {
            // 모든 사용자의 스코프 포인트 계산
            List<Map<String, Object>> userPoints = userRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("points", calculateUserScopePoints(user.getId(), scopeType));
                    return map;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("points"), (Integer) a.get("points")))
                .collect(Collectors.toList());
            
            // 해당 사용자의 순위 찾기
            for (int i = 0; i < userPoints.size(); i++) {
                if (userPoints.get(i).get("userId").equals(userId)) {
                    return i + 1;
                }
            }
            
            return userPoints.size(); // 마지막 순위
            
        } catch (Exception e) {
            log.warn("사용자 {} 스코프 {} 순위 계산 실패: {}", userId, scopeType, e.getMessage());
            return 1;
        }
    }
    
    /**
     * 스코프 시작 시간 계산
     */
    private LocalDateTime getScopeStartTime(String scopeType) {
        LocalDateTime now = LocalDateTime.now();
        
        switch (scopeType.toUpperCase()) {
            case "DAILY":
                return now.toLocalDate().atStartOfDay();
            case "WEEKLY":
                return now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
            case "MONTHLY":
                return now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            case "TOTAL":
                return LocalDateTime.of(2000, 1, 1, 0, 0); // 매우 과거
            default:
                return now.toLocalDate().atStartOfDay();
        }
    }
    
    /**
     * 스코프별 총 사용자 수
     */
    private int getTotalUsersInScope(String scopeType) {
        try {
            return (int) userRepository.count();
        } catch (Exception e) {
            log.warn("스코프 {} 총 사용자 수 조회 실패: {}", scopeType, e.getMessage());
            return 0;
        }
    }
}
