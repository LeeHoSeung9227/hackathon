package com.hackathon.repository;

import com.hackathon.entity.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long> {
    
    /**
     * 사용자별 교환 내역 조회
     */
    List<ExchangeHistory> findByUserId(Long userId);
    
    /**
     * 상품별 교환 내역 조회
     */
    List<ExchangeHistory> findByProductId(Long productId);
    
    /**
     * 사용자별 상품별 교환 내역 조회
     */
    List<ExchangeHistory> findByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * 사용자별 날짜 범위 교환 내역 조회
     */
    @Query("SELECT eh FROM ExchangeHistory eh WHERE eh.userId = :userId AND eh.exchangedAt BETWEEN :startDate AND :endDate")
    List<ExchangeHistory> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    /**
     * 상품별 교환 횟수 조회
     */
    @Query("SELECT COUNT(eh) FROM ExchangeHistory eh WHERE eh.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * 사용자별 교환 횟수 조회
     */
    @Query("SELECT COUNT(eh) FROM ExchangeHistory eh WHERE eh.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * 날짜 범위별 모든 교환 내역 조회
     */
    @Query("SELECT eh FROM ExchangeHistory eh WHERE eh.exchangedAt BETWEEN :startDate AND :endDate")
    List<ExchangeHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * 최근 교환 내역 조회 (최신순)
     */
    @Query("SELECT eh FROM ExchangeHistory eh ORDER BY eh.exchangedAt DESC")
    List<ExchangeHistory> findRecentExchanges();
    
    /**
     * 사용자별 최근 교환 내역 조회
     */
    @Query("SELECT eh FROM ExchangeHistory eh WHERE eh.userId = :userId ORDER BY eh.exchangedAt DESC")
    List<ExchangeHistory> findRecentExchangesByUserId(@Param("userId") Long userId);
    
    /**
     * 상품별 최근 교환 내역 조회
     */
    @Query("SELECT eh FROM ExchangeHistory eh WHERE eh.productId = :productId ORDER BY eh.exchangedAt DESC")
    List<ExchangeHistory> findRecentExchangesByProductId(@Param("productId") Long productId);
}
