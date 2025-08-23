package com.hackathon.repository.b;

import com.hackathon.entity.b.Badge;
import com.hackathon.entity.b.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByCategory(String category);
    List<Badge> findByPointsRequiredLessThanEqual(Integer points);
    List<Badge> findByCategoryAndPointsRequiredLessThanEqual(String category, Integer points);
    
    // 간단한 쿼리로 수정 - UserBadge 테이블에서 사용자 ID로 조회
    @Query("SELECT ub.badgeId as badgeId, '테스트 뱃지' as badgeName, ub.earnedAt as earnedAt " +
           "FROM UserBadge ub WHERE ub.userId = :userId")
    List<Object[]> findUserBadgesByUserId(@Param("userId") Long userId);
}
