package com.hackathon.service.b;

import com.hackathon.entity.a.PointHistory;
import com.hackathon.entity.a.User;
import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.repository.a.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /* ===== 내부 집계 유틸 ===== */
    private static class Totals {
        final int earnedTotal;     // 히스토리 양수 합
        final int spentTotal;      // 히스토리 음수 절대값 합
        final int balance;         // 현재 잔액 = newTotalPoints = User.pointsTotal
        final int accumulatedTotal;// 누적(획득) 총합 = balance + spentTotal
        Totals(int e, int s, int b) { this.earnedTotal = e; this.spentTotal = s; this.balance = b; this.accumulatedTotal = b + s; }
    }

    /** 사용자 누적/사용/잔액 집계(TOTAL) */
    private Totals computeUserTotals(Long userId) {
        try {
            LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime end   = LocalDateTime.now();

            List<PointHistory> histories = pointHistoryRepository
                    .findByUserIdAndCreatedAtBetween(userId, start, end);

            int earned = 0, spent = 0;
            for (PointHistory ph : histories) {
                int p = ph.getPoints();
                if (p > 0) earned += p;
                else if (p < 0) spent += -p;
            }

            // balance = 현재 잔액(newTotalPoints) = User.pointsTotal
            int balance = userRepository.findById(userId)
                    .map(u -> Optional.ofNullable(u.getPointsTotal()).orElse(0))
                    .orElse(0);

            return new Totals(earned, spent, balance);
        } catch (Exception e) {
            log.warn("computeUserTotals 실패 userId={}, err={}", userId, e.getMessage());
            return new Totals(0, 0, 0);
        }
    }

    private void validateLimit(int limit) {
        if (limit != 30 && limit != 100) {
            throw new IllegalArgumentException("limit은 30 또는 100만 허용됩니다.");
        }
    }

    /** 개인 Top 30/100 — 정렬: accumulatedTotal(desc) */
    public List<Map<String, Object>> getTopIndividuals(int limit) {
        validateLimit(limit);

        List<User> users = userRepository.findAll();

        List<Map<String, Object>> ranked = users.stream()
                .map(u -> {
                    Totals t = computeUserTotals(u.getId());
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("userId", u.getId());
                    row.put("username", u.getUsername());
                    row.put("name", u.getName());
                    row.put("college", u.getCollege());
                    row.put("campus", u.getCampus());
                    // 상세 지표 모두 포함
                    row.put("earnedTotal", t.earnedTotal);         // 히스토리 양수 합
                    row.put("spentTotal", t.spentTotal);           // 히스토리 음수 절대 합
                    row.put("newTotalPoints", t.balance);          // = 현재 잔액 = PointController의 newTotalPoints
                    row.put("accumulatedTotal", t.accumulatedTotal);// = balance + spentTotal
                    row.put("scopeType", "TOTAL");
                    return row;
                })
                .sorted((a, b) -> Integer.compare(
                        (int) b.get("accumulatedTotal"),
                        (int) a.get("accumulatedTotal")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) ranked.get(i).put("rank", i + 1);
        return ranked;
    }

    /** 단과대 Top 30/100 — 정렬: accumulatedTotal 합(desc) */
    public List<Map<String, Object>> getTopColleges(int limit) {
        validateLimit(limit);

        List<User> users = userRepository.findAll();
        Map<String, List<User>> byCollege = users.stream()
                .filter(u -> u.getCollege() != null && !u.getCollege().isBlank())
                .collect(Collectors.groupingBy(User::getCollege));

        List<Map<String, Object>> ranked = byCollege.entrySet().stream()
                .map(e -> {
                    String college = e.getKey();
                    int earnedSum = 0, spentSum = 0, balanceSum = 0;
                    for (User u : e.getValue()) {
                        Totals t = computeUserTotals(u.getId());
                        earnedSum  += t.earnedTotal;
                        spentSum   += t.spentTotal;
                        balanceSum += t.balance;
                    }
                    int accumulatedSum = balanceSum + spentSum;

                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("college", college);
                    row.put("members", e.getValue().size());
                    row.put("earnedTotal", earnedSum);
                    row.put("spentTotal", spentSum);
                    row.put("newTotalPoints", balanceSum);
                    row.put("accumulatedTotal", accumulatedSum); // 정렬 기준
                    row.put("scopeType", "TOTAL");
                    return row;
                })
                .sorted((a, b) -> Integer.compare(
                        (int) b.get("accumulatedTotal"),
                        (int) a.get("accumulatedTotal")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) ranked.get(i).put("rank", i + 1);
        return ranked;
    }

    /** 캠퍼스 Top 30/100 — 정렬: accumulatedTotal 합(desc) */
    public List<Map<String, Object>> getTopCampuses(int limit) {
        validateLimit(limit);

        List<User> users = userRepository.findAll();
        Map<String, List<User>> byCampus = users.stream()
                .filter(u -> u.getCampus() != null && !u.getCampus().isBlank())
                .collect(Collectors.groupingBy(User::getCampus));

        List<Map<String, Object>> ranked = byCampus.entrySet().stream()
                .map(e -> {
                    String campus = e.getKey();
                    int earnedSum = 0, spentSum = 0, balanceSum = 0;
                    for (User u : e.getValue()) {
                        Totals t = computeUserTotals(u.getId());
                        earnedSum  += t.earnedTotal;
                        spentSum   += t.spentTotal;
                        balanceSum += t.balance;
                    }
                    int accumulatedSum = balanceSum + spentSum;

                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("campus", campus);
                    row.put("members", e.getValue().size());
                    row.put("earnedTotal", earnedSum);
                    row.put("spentTotal", spentSum);
                    row.put("newTotalPoints", balanceSum);
                    row.put("accumulatedTotal", accumulatedSum); // 정렬 기준
                    row.put("scopeType", "TOTAL");
                    return row;
                })
                .sorted((a, b) -> Integer.compare(
                        (int) b.get("accumulatedTotal"),
                        (int) a.get("accumulatedTotal")))
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) ranked.get(i).put("rank", i + 1);
        return ranked;
    }
}
