package com.hackathon.repository;

import com.hackathon.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    
    /**
     * 범위 타입별 랭킹 조회 (순위순)
     */
    List<Ranking> findByScopeTypeOrderByRankPosition(String scopeType);
    
    /**
     * 사용자별 랭킹 조회
     */
    List<Ranking> findByUserId(Long userId);
    
    /**
     * 사용자별 특정 범위 랭킹 조회
     */
    List<Ranking> findByUserIdAndScopeType(Long userId, String scopeType);
    
    /**
     * 상위 N명 랭킹 조회 (LIMIT 적용)
     */
    @Query(value = "SELECT * FROM rankings WHERE scope_type = :scopeType ORDER BY rank_position ASC LIMIT :topN", nativeQuery = true)
    List<Ranking> findTopNByScopeType(@Param("scopeType") String scopeType, @Param("topN") Integer topN);
    
    /**
     * 특정 순위의 랭킹 조회
     */
    @Query("SELECT r FROM Ranking r WHERE r.scopeType = :scopeType AND r.rankPosition = :rankPosition")
    List<Ranking> findByScopeTypeAndRankPosition(@Param("scopeType") String scopeType, 
                                                 @Param("rankPosition") Integer rankPosition);
    
    /**
     * 특정 순위 범위의 랭킹 조회
     */
    @Query("SELECT r FROM Ranking r WHERE r.scopeType = :scopeType AND r.rankPosition BETWEEN :startRank AND :endRank ORDER BY r.rankPosition ASC")
    List<Ranking> findByScopeTypeAndRankRange(@Param("scopeType") String scopeType, 
                                              @Param("startRank") Integer startRank, 
                                              @Param("endRank") Integer endRank);
    
    /**
     * 보상별 랭킹 조회
     */
    @Query("SELECT r FROM Ranking r WHERE r.reward >= :minReward ORDER BY r.reward DESC")
    List<Ranking> findByMinReward(@Param("minReward") Integer minReward);
    
    /**
     * 범위 타입별 최고 보상 랭킹 조회
     */
    @Query("SELECT r FROM Ranking r WHERE r.scopeType = :scopeType ORDER BY r.reward DESC")
    List<Ranking> findByScopeTypeOrderByRewardDesc(@Param("scopeType") String scopeType);
    
    /**
     * 사용자별 최고 순위 랭킹 조회
     */
    @Query("SELECT r FROM Ranking r WHERE r.userId = :userId ORDER BY r.rankPosition ASC")
    List<Ranking> findByUserIdOrderByRankPosition(@Param("userId") Long userId);
    
    /**
     * 특정 범위의 랭킹 개수 조회
     */
    @Query("SELECT COUNT(r) FROM Ranking r WHERE r.scopeType = :scopeType")
    Long countByScopeType(@Param("scopeType") String scopeType);
}
