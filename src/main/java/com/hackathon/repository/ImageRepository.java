package com.hackathon.repository;

import com.hackathon.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    /**
     * 사용자별 이미지 조회
     */
    List<Image> findByUserId(Long userId);
    
    /**
     * 상태별 이미지 조회
     */
    List<Image> findByStatus(String status);
    
    /**
     * 사용자별 상태별 이미지 조회
     */
    List<Image> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * 날짜 범위별 이미지 조회
     */
    @Query("SELECT i FROM Image i WHERE i.capturedAt BETWEEN :startDate AND :endDate")
    List<Image> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    /**
     * 사용자별 날짜 범위별 이미지 조회
     */
    @Query("SELECT i FROM Image i WHERE i.userId = :userId AND i.capturedAt BETWEEN :startDate AND :endDate")
    List<Image> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                         @Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    /**
     * 특정 상태의 이미지 개수 조회
     */
    @Query("SELECT COUNT(i) FROM Image i WHERE i.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * 사용자별 이미지 개수 조회
     */
    @Query("SELECT COUNT(i) FROM Image i WHERE i.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * 사용자별 상태별 이미지 개수 조회
     */
    @Query("SELECT COUNT(i) FROM Image i WHERE i.userId = :userId AND i.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    /**
     * 최근 이미지 조회 (최신순)
     */
    @Query("SELECT i FROM Image i ORDER BY i.capturedAt DESC")
    List<Image> findRecentImages();
    
    /**
     * 사용자별 최근 이미지 조회
     */
    @Query("SELECT i FROM Image i WHERE i.userId = :userId ORDER BY i.capturedAt DESC")
    List<Image> findRecentImagesByUserId(@Param("userId") Long userId);
}
