package com.hackathon.repository;

import com.hackathon.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    
    /**
     * 사용자별 포인트 내역 조회
     */
    List<PointHistory> findByUserId(Long userId);
    
    /**
     * 이미지별 포인트 내역 조회
     */
    List<PointHistory> findByImagesId(Long imagesId);
    
    /**
     * 사용자별 변경 타입별 포인트 내역 조회
     */
    List<PointHistory> findByUserIdAndChangeType(Long userId, String changeType);
    
    /**
     * 사용자별 날짜 범위 포인트 내역 조회
     */
    @Query("SELECT ph FROM PointHistory ph WHERE ph.userId = :userId AND ph.createdAt BETWEEN :startDate AND :endDate")
    List<PointHistory> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    /**
     * 사용자별 포인트 합계 조회
     */
    @Query("SELECT SUM(ph.amount) FROM PointHistory ph WHERE ph.userId = :userId")
    Integer sumAmountByUserId(@Param("userId") Long userId);
    
    /**
     * 변경 타입별 포인트 내역 조회
     */
    List<PointHistory> findByChangeType(String changeType);
    
    /**
     * 특정 금액 이상의 포인트 내역 조회
     */
    @Query("SELECT ph FROM PointHistory ph WHERE ph.amount >= :minAmount")
    List<PointHistory> findByMinAmount(@Param("minAmount") Integer minAmount);
    
    /**
     * 사용자별 특정 금액 이상의 포인트 내역 조회
     */
    @Query("SELECT ph FROM PointHistory ph WHERE ph.userId = :userId AND ph.amount >= :minAmount")
    List<PointHistory> findByUserIdAndMinAmount(@Param("userId") Long userId, @Param("minAmount") Integer minAmount);
    
    /**
     * 날짜 범위별 모든 포인트 내역 조회
     */
    @Query("SELECT ph FROM PointHistory ph WHERE ph.createdAt BETWEEN :startDate AND :endDate")
    List<PointHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
}
