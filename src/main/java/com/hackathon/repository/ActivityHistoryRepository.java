package com.hackathon.repository;

import com.hackathon.entity.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
    
    /**
     * 사용자별 활동 기록 조회
     */
    List<ActivityHistory> findByUserId(Long userId);
    
    /**
     * 사용자별 특정 날짜 활동 기록 조회
     */
    List<ActivityHistory> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);
    
    /**
     * 사용자별 활동 타입별 기록 조회
     */
    List<ActivityHistory> findByUserIdAndActivityType(Long userId, String activityType);
    
    /**
     * 사용자별 날짜 범위 활동 기록 조회
     */
    @Query("SELECT ah FROM ActivityHistory ah WHERE ah.userId = :userId AND ah.activityDate BETWEEN :startDate AND :endDate")
    List<ActivityHistory> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
    
    /**
     * 특정 날짜 포인트 합계 조회
     */
    @Query("SELECT SUM(ah.points) FROM ActivityHistory ah WHERE ah.userId = :userId AND ah.activityDate = :date")
    Integer sumPointsByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    /**
     * 활동 타입별 기록 조회
     */
    List<ActivityHistory> findByActivityType(String activityType);
    
    /**
     * 특정 배지 ID로 활동 기록 조회
     */
    List<ActivityHistory> findByBadgeId(Long badgeId);
    
    /**
     * 사용자별 배지별 활동 기록 조회
     */
    List<ActivityHistory> findByUserIdAndBadgeId(Long userId, Long badgeId);
    
    /**
     * 날짜 범위별 모든 활동 기록 조회
     */
    @Query("SELECT ah FROM ActivityHistory ah WHERE ah.activityDate BETWEEN :startDate AND :endDate")
    List<ActivityHistory> findByDateRange(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
}
