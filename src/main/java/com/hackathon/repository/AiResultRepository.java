package com.hackathon.repository;

import com.hackathon.entity.AiResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AiResultRepository extends JpaRepository<AiResult, Long> {
    
    /**
     * 이미지별 AI 분석 결과 조회
     */
    Optional<AiResult> findByImagesId(Long imagesId);
    
    /**
     * 사용자별 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar JOIN Image i ON ar.imagesId = i.id WHERE i.userId = :userId")
    List<AiResult> findByUserId(@Param("userId") Long userId);
    
    /**
     * 재질 타입별 AI 분석 결과 조회
     */
    List<AiResult> findByMaterialType(String materialType);
    
    /**
     * 승인 상태별 AI 분석 결과 조회
     */
    List<AiResult> findByIsApproved(Boolean isApproved);
    
    /**
     * 날짜 범위별 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar WHERE ar.analyzedAt BETWEEN :startDate AND :endDate")
    List<AiResult> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    /**
     * 최소 신뢰도별 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar WHERE ar.confidence >= :minConfidence")
    List<AiResult> findByMinConfidence(@Param("minConfidence") Double minConfidence);
    
    /**
     * 승인되지 않은 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar WHERE ar.isApproved = false")
    List<AiResult> findUnapprovedResults();
    
    /**
     * 승인된 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar WHERE ar.isApproved = true")
    List<AiResult> findApprovedResults();
    
    /**
     * 특정 신뢰도 범위의 AI 분석 결과 조회
     */
    @Query("SELECT ar FROM AiResult ar WHERE ar.confidence BETWEEN :minConfidence AND :maxConfidence")
    List<AiResult> findByConfidenceRange(@Param("minConfidence") Double minConfidence, 
                                         @Param("maxConfidence") Double maxConfidence);
    
    /**
     * 재질 타입별 평균 신뢰도 조회
     */
    @Query("SELECT ar.materialType, AVG(ar.confidence) FROM AiResult ar GROUP BY ar.materialType")
    List<Object[]> findAverageConfidenceByMaterialType();
}
