package com.hackathon.repository;

import com.hackathon.entity.AuthLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthLoginRepository extends JpaRepository<AuthLogin, Long> {
    
    /**
     * 토큰으로 로그인 세션 조회
     */
    Optional<AuthLogin> findByToken(String token);
    
    /**
     * 활성 토큰으로 로그인 세션 조회
     */
    Optional<AuthLogin> findByTokenAndIsActiveTrue(String token);
    
    /**
     * 사용자별 로그인 세션 조회
     */
    List<AuthLogin> findByUserId(Long userId);
    
    /**
     * 사용자별 활성 로그인 세션 조회
     */
    List<AuthLogin> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * 사용자별 로그인 세션 삭제
     */
    void deleteByUserId(Long userId);
    
    /**
     * 만료된 세션 조회
     */
    @Query("SELECT al FROM AuthLogin al WHERE al.loginAt < :cutoffDate AND al.isActive = true")
    List<AuthLogin> findExpiredSessions(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * 활성 세션 개수 조회
     */
    @Query("SELECT COUNT(al) FROM AuthLogin al WHERE al.isActive = true")
    Long countActiveSessions();
    
    /**
     * 사용자별 활성 세션 개수 조회
     */
    @Query("SELECT COUNT(al) FROM AuthLogin al WHERE al.userId = :userId AND al.isActive = true")
    Long countActiveSessionsByUserId(@Param("userId") Long userId);
    
    /**
     * 특정 시간 이후 로그인한 세션 조회
     */
    @Query("SELECT al FROM AuthLogin al WHERE al.loginAt >= :since")
    List<AuthLogin> findSessionsSince(@Param("since") LocalDateTime since);
    
    /**
     * 사용자별 특정 시간 이후 로그인한 세션 조회
     */
    @Query("SELECT al FROM AuthLogin al WHERE al.userId = :userId AND al.loginAt >= :since")
    List<AuthLogin> findSessionsSinceByUserId(@Param("userId") Long userId, @Param("since") LocalDateTime since);
}
