package com.hackathon.repository;

import com.hackathon.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    
    /**
     * 카테고리별 뱃지 조회
     */
    List<Badge> findByCategory(String category);
    
    /**
     * 포인트 요구사항 이하의 뱃지 조회
     */
    List<Badge> findByRequiredPointsLessThanEqual(Integer requiredPoints);
    
    /**
     * 특정 포인트 범위의 뱃지 조회
     */
    @Query("SELECT b FROM Badge b WHERE b.requiredPoints BETWEEN :minPoints AND :maxPoints")
    List<Badge> findByPointsRange(@Param("minPoints") Integer minPoints, @Param("maxPoints") Integer maxPoints);
    
    /**
     * 이름으로 뱃지 검색
     */
    List<Badge> findByNameContainingIgnoreCase(String name);
}
