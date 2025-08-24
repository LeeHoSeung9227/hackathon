package com.hackathon.service.b;

import com.hackathon.dto.b.BadgeDto;
import com.hackathon.entity.b.Badge;
import com.hackathon.entity.b.UserBadge;
import com.hackathon.repository.b.BadgeRepository;
import com.hackathon.repository.b.UserBadgeRepository;
import com.hackathon.service.a.PointHistoryService;
import com.hackathon.service.a.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BadgeService {
    
    private static final Logger log = LoggerFactory.getLogger(BadgeService.class);
    
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final PointHistoryService pointHistoryService;
    private final UserService userService;
    
    public BadgeService(BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository, PointHistoryService pointHistoryService, UserService userService) {
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.pointHistoryService = pointHistoryService;
        this.userService = userService;
    }
    
    public BadgeDto createBadge(String name, String description, String imageUrl, Integer pointsReward, String category, String conditionType, Integer conditionValue, String conditionDescription) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setImageUrl(imageUrl);
        badge.setPointsReward(pointsReward);
        badge.setCategory(category);
        badge.setConditionType(conditionType);
        badge.setConditionValue(conditionValue);
        badge.setConditionDescription(conditionDescription);
        
        Badge savedBadge = badgeRepository.save(badge);
        return convertToDto(savedBadge);
    }
    
    private BadgeDto convertToDto(Badge badge) {
        BadgeDto dto = new BadgeDto();
        dto.setId(badge.getId());
        dto.setName(badge.getName());
        dto.setDescription(badge.getDescription());
        dto.setImageUrl(badge.getImageUrl());
        dto.setPointsReward(badge.getPointsReward());
        dto.setCategory(badge.getCategory());
        dto.setConditionType(badge.getConditionType());
        dto.setConditionValue(badge.getConditionValue());
        dto.setConditionDescription(badge.getConditionDescription());
        dto.setCreatedAt(badge.getCreatedAt());
        dto.setUpdatedAt(badge.getUpdatedAt());
        return dto;
    }
    
    // 모든 뱃지 조회
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }
    
    // 사용자별 뱃지 조회
    public List<UserBadge> getUserBadges(Long userId) {
        return userBadgeRepository.findByUserId(userId);
    }
    
    // 뱃지 획득 처리
    public void earnBadge(Long userId, Long badgeId) {
        try {
            // 이미 획득한 뱃지인지 확인
            Optional<UserBadge> existingBadge = userBadgeRepository.findByUserIdAndBadgeId(userId, badgeId);
            if (existingBadge.isPresent() && existingBadge.get().isEarned()) {
                log.info("사용자 {}는 이미 뱃지 {}를 획득했습니다.", userId, badgeId);
                return;
            }
            
            // 뱃지 정보 조회
            Optional<Badge> badgeOpt = badgeRepository.findById(badgeId);
            if (badgeOpt.isEmpty()) {
                log.error("뱃지 {}를 찾을 수 없습니다.", badgeId);
                return;
            }
            
            Badge badge = badgeOpt.get();
            
            // UserBadge 생성 또는 업데이트
            UserBadge userBadge = existingBadge.orElse(new UserBadge());
            userBadge.setUserId(userId);
            userBadge.setBadgeId(badgeId);
            userBadge.setEarned(true);
            userBadge.setEarnedAt(LocalDateTime.now());
            
            userBadgeRepository.save(userBadge);
            
            // 포인트 적립
            pointHistoryService.createPointHistory(userId, "BADGE_EARNED", badge.getPointsReward(), 
                "뱃지 획득: " + badge.getName());
            
            log.info("사용자 {}가 뱃지 {}를 획득했습니다. 포인트 {} 적립", userId, badge.getName(), badge.getPointsReward());
            
        } catch (Exception e) {
            log.error("뱃지 획득 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    // AI 분석 완료 후 뱃지 획득 조건 확인
    public void checkBadgeConditionsAfterAnalysis(Long userId) {
        try {
            log.info("🔍 뱃지 획득 조건 체크 시작: 사용자 {}", userId);
            
            // AI 분석 횟수 기반 뱃지 확인
            checkAnalysisCountBadges(userId);
            
            // 총 포인트 기반 뱃지 확인
            checkTotalPointsBadges(userId);
            
            log.info("✅ 뱃지 조건 체크 완료: 사용자 {}", userId);
            
        } catch (Exception e) {
            log.error("❌ 뱃지 조건 확인 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    // AI 분석 횟수 기반 뱃지 확인
    private void checkAnalysisCountBadges(Long userId) {
        try {
            // AI 분석 타입의 포인트 히스토리 조회하여 분석 횟수 계산
            List<com.hackathon.dto.a.PointHistoryDto> aiAnalysisHistory = 
                pointHistoryService.getPointHistoryByUserIdAndType(userId, "AI_ANALYSIS");
            
            int analysisCount = aiAnalysisHistory.size();
            log.info("📊 사용자 {}의 AI 분석 횟수: {}", userId, analysisCount);
            
            // AI 분석 횟수 기반 뱃지들 조회
            List<Badge> analysisBadges = badgeRepository.findByConditionType("AI_ANALYSIS_COUNT");
            
            for (Badge badge : analysisBadges) {
                if (analysisCount >= badge.getConditionValue()) {
                    // 뱃지 획득 조건 충족 - 자동으로 뱃지 지급
                    earnBadge(userId, badge.getId());
                    log.info("🎉 사용자 {}가 AI 분석 횟수 뱃지 {} 자동 획득! (분석횟수: {}, 필요횟수: {})", 
                        userId, badge.getName(), analysisCount, badge.getConditionValue());
                }
            }
            
        } catch (Exception e) {
            log.error("❌ AI 분석 횟수 기반 뱃지 확인 중 오류: {}", e.getMessage(), e);
        }
    }
    
    // 총 포인트 기반 뱃지 확인
    private void checkTotalPointsBadges(Long userId) {
        try {
            // 사용자의 총 포인트 조회
            var user = userService.getUserById(userId);
            int totalPoints = user.getPointsTotal();
            
            log.info("📊 사용자 {}의 총 포인트: {}", userId, totalPoints);
            
            // 총 포인트 기반 뱃지들 조회
            List<Badge> pointsBadges = badgeRepository.findByConditionType("TOTAL_POINTS");
            
            for (Badge badge : pointsBadges) {
                if (totalPoints >= badge.getConditionValue()) {
                    // 뱃지 획득 조건 충족 - 자동으로 뱃지 지급
                    earnBadge(userId, badge.getId());
                    log.info("🎉 사용자 {}가 총 포인트 뱃지 {} 자동 획득! (현재포인트: {}, 필요포인트: {})", 
                        userId, badge.getName(), totalPoints, badge.getConditionValue());
                }
            }
            
        } catch (Exception e) {
            log.error("❌ 총 포인트 기반 뱃지 확인 중 오류: {}", e.getMessage(), e);
        }
    }
    
    // 재활용 횟수 기반 뱃지 확인 (새로 추가)
    public void checkRecyclingCountBadges(Long userId) {
        try {
            // 재활용 가능한 AI 분석 결과 조회
            List<com.hackathon.dto.a.PointHistoryDto> recyclingHistory = 
                pointHistoryService.getPointHistoryByUserIdAndType(userId, "AI_ANALYSIS");
            
            int recyclingCount = recyclingHistory.size();
            log.info("📊 사용자 {}의 재활용 횟수: {}", userId, recyclingCount);
            
            // 재활용 횟수 기반 뱃지들 조회
            List<Badge> recyclingBadges = badgeRepository.findByConditionType("RECYCLING_COUNT");
            
            for (Badge badge : recyclingBadges) {
                if (recyclingCount >= badge.getConditionValue()) {
                    // 뱃지 획득 조건 충족 - 자동으로 뱃지 지급
                    earnBadge(userId, badge.getId());
                    log.info("🎉 사용자 {}가 재활용 횟수 뱃지 {} 자동 획득! (재활용횟수: {}, 필요횟수: {})", 
                        userId, badge.getName(), recyclingCount, badge.getConditionValue());
                }
            }
            
        } catch (Exception e) {
            log.error("❌ 재활용 횟수 기반 뱃지 확인 중 오류: {}", e.getMessage(), e);
        }
    }
    
    // 기본 뱃지들 생성 (초기화용)
    public void initializeDefaultBadges() {
        try {
            // 기존 뱃지가 있는지 확인
            if (badgeRepository.count() > 0) {
                log.info("ℹ️ 기본 뱃지가 이미 존재합니다. 초기화를 건너뜁니다.");
                return;
            }
            
            log.info("🚀 기본 뱃지 초기화 시작");
            
            // AI 분석 횟수 기반 뱃지들
            createBadge("첫 번째 분석", "첫 번째 AI 이미지 분석을 완료했습니다", "🥇", 10, "AI_ANALYSIS", "AI_ANALYSIS_COUNT", 1, "AI 이미지 분석 1회 완료");
            createBadge("분석 마스터", "10번의 AI 이미지 분석을 완료했습니다", "🥈", 50, "AI_ANALYSIS", "AI_ANALYSIS_COUNT", 10, "AI 이미지 분석 10회 완료");
            createBadge("분석 전문가", "50번의 AI 이미지 분석을 완료했습니다", "🥉", 100, "AI_ANALYSIS", "AI_ANALYSIS_COUNT", 50, "AI 이미지 분석 50회 완료");
            createBadge("분석 챔피언", "100번의 AI 이미지 분석을 완료했습니다", "🏆", 200, "AI_ANALYSIS", "AI_ANALYSIS_COUNT", 100, "AI 이미지 분석 100회 완료");
            
            // 총 포인트 기반 뱃지들
            createBadge("포인트 수집가", "100포인트를 모았습니다", "💰", 20, "POINTS", "TOTAL_POINTS", 100, "총 포인트 100점 달성");
            createBadge("포인트 마스터", "500포인트를 모았습니다", "💎", 50, "POINTS", "TOTAL_POINTS", 500, "총 포인트 500점 달성");
            createBadge("포인트 전문가", "1000포인트를 모았습니다", "💍", 100, "POINTS", "TOTAL_POINTS", 1000, "총 포인트 1000점 달성");
            createBadge("포인트 챔피언", "2000포인트를 모았습니다", "👑", 200, "POINTS", "TOTAL_POINTS", 2000, "총 포인트 2000점 달성");
            
            // 재활용 횟수 기반 뱃지들
            createBadge("재활용 초보", "5번의 재활용을 완료했습니다", "♻️", 25, "RECYCLING", "RECYCLING_COUNT", 5, "재활용 5회 완료");
            createBadge("재활용 마스터", "20번의 재활용을 완료했습니다", "♻️♻️", 75, "RECYCLING", "RECYCLING_COUNT", 20, "재활용 20회 완료");
            createBadge("재활용 전문가", "50번의 재활용을 완료했습니다", "♻️♻️♻️", 150, "RECYCLING", "RECYCLING_COUNT", 50, "재활용 50회 완료");
            createBadge("재활용 챔피언", "100번의 재활용을 완료했습니다", "♻️🏆", 300, "RECYCLING", "RECYCLING_COUNT", 100, "재활용 100회 완료");
            
            log.info("✅ 기본 뱃지 초기화 완료: {}개 뱃지 생성", badgeRepository.count());
            
        } catch (Exception e) {
            log.error("❌ 기본 뱃지 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    // 뱃지 획득 조건 체크 (모든 조건)
    public void checkAllBadgeConditions(Long userId) {
        try {
            log.info("🔍 모든 뱃지 획득 조건 체크 시작: 사용자 {}", userId);
            
            checkAnalysisCountBadges(userId);
            checkTotalPointsBadges(userId);
            checkRecyclingCountBadges(userId);
            
            log.info("✅ 모든 뱃지 조건 체크 완료: 사용자 {}", userId);
            
        } catch (Exception e) {
            log.error("❌ 모든 뱃지 조건 체크 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
