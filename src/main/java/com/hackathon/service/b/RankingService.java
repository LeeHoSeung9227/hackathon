package com.hackathon.service.b;

import com.hackathon.entity.a.PointHistory;
import com.hackathon.entity.a.User;
import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.repository.a.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RankingService {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);
    
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    
    public RankingService(UserRepository userRepository, PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    /** 개인 Top 30/100 (TOTAL 고정: 기간 전체 합계) */
    public List<Map<String, Object>> getTopIndividuals(int limit) {
        validateLimit(limit);
        final String timeScope = "TOTAL";

        List<User> users = userRepository.findAll();

        List<Map<String, Object>> ranked = users.stream()
                .map(u -> {
                    int points = sumUserPointsTotal(u.getId()); // TOTAL 합계
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("userId", u.getId());
                    row.put("username", u.getUsername());
                    row.put("name", u.getName());
                    row.put("college", u.getCollege());
                    row.put("campus", u.getCampus());
                    row.put("points", points);
                    return row;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("points"), (Integer) a.get("points")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) {
            ranked.get(i).put("rank", i + 1);
            ranked.get(i).put("scopeType", timeScope);
        }
        return ranked;
    }

    /** 단과대 Top 30/100 (TOTAL 고정: 단과대 합계) */
    public List<Map<String, Object>> getTopColleges(int limit) {
        validateLimit(limit);
        final String timeScope = "TOTAL";

        List<User> users = userRepository.findAll();
        Map<String, List<User>> byCollege = users.stream()
                .filter(u -> u.getCollege() != null && !u.getCollege().isBlank())
                .collect(Collectors.groupingBy(User::getCollege));

        List<Map<String, Object>> ranked = byCollege.entrySet().stream()
                .map(e -> {
                    String college = e.getKey();
                    List<User> group = e.getValue();
                    int score = group.stream()
                            .mapToInt(u -> sumUserPointsTotal(u.getId()))
                            .sum();

                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("college", college);
                    row.put("members", group.size());
                    row.put("score", score);
                    row.put("scopeType", timeScope);
                    return row;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("score"), (Integer) a.get("score")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) ranked.get(i).put("rank", i + 1);
        return ranked;
    }

    /** 캠퍼스 Top 30/100 (TOTAL 고정: 캠퍼스 합계) */
    public List<Map<String, Object>> getTopCampuses(int limit) {
        validateLimit(limit);
        final String timeScope = "TOTAL";

        List<User> users = userRepository.findAll();
        Map<String, List<User>> byCampus = users.stream()
                .filter(u -> u.getCampus() != null && !u.getCampus().isBlank())
                .collect(Collectors.groupingBy(User::getCampus));

        List<Map<String, Object>> ranked = byCampus.entrySet().stream()
                .map(e -> {
                    String campus = e.getKey();
                    List<User> group = e.getValue();
                    int score = group.stream()
                            .mapToInt(u -> sumUserPointsTotal(u.getId()))
                            .sum();

                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("campus", campus);
                    row.put("members", group.size());
                    row.put("score", score);
                    row.put("scopeType", timeScope);
                    return row;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("score"), (Integer) a.get("score")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) ranked.get(i).put("rank", i + 1);
        return ranked;
    }

    /* ===== 내부 유틸 ===== */

    private void validateLimit(int limit) {
        if (limit != 30 && limit != 100) {
            throw new IllegalArgumentException("limit은 30 또는 100만 허용됩니다.");
        }
    }

    /** TOTAL 기간 누적 획득 포인트 합계 */
    private int sumUserPointsTotal(Long userId) {
        try {
            // TOTAL: 아주 과거 ~ 현재 (획득한 포인트만)
            LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.now();
            List<PointHistory> histories = pointHistoryRepository
                    .findByUserIdAndCreatedAtBetween(userId, start, end);
            
            // 양수 포인트만 합산 (획득한 포인트만)
            int totalEarnedPoints = histories.stream()
                    .filter(history -> history.getPoints() > 0)
                    .mapToInt(PointHistory::getPoints)
                    .sum();
            
            log.info("유저 {} 누적 획득 포인트: {}", userId, totalEarnedPoints);
            return totalEarnedPoints;
            
        } catch (Exception e) {
            log.warn("누적 획득 포인트 계산 실패 userId={}, err={}", userId, e.getMessage());
            return 0;
        }
    }
}
