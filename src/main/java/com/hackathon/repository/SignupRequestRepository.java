package com.hackathon.repository;

import com.hackathon.entity.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignupRequestRepository extends JpaRepository<SignupRequest, Long> {
    
    /**
     * 사용자명으로 가입 신청 조회
     */
    Optional<SignupRequest> findByUsername(String username);
    
    /**
     * 이메일로 가입 신청 조회
     */
    Optional<SignupRequest> findByEmail(String email);
    
    /**
     * 사용자명 존재 여부 확인
     */
    boolean existsByUsername(String username);
    
    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
    
    /**
     * 인증 상태별 가입 신청 조회
     */
    List<SignupRequest> findByIsVerified(Boolean isVerified);
    
    /**
     * 인증되지 않은 가입 신청 조회
     */
    @Query("SELECT sr FROM SignupRequest sr WHERE sr.isVerified = false")
    List<SignupRequest> findUnverifiedRequests();
    
    /**
     * 인증된 가입 신청 조회
     */
    @Query("SELECT sr FROM SignupRequest sr WHERE sr.isVerified = true")
    List<SignupRequest> findVerifiedRequests();
    
    /**
     * 특정 날짜 이후 생성된 가입 신청 조회
     */
    @Query("SELECT sr FROM SignupRequest sr WHERE sr.createdAt >= :date")
    List<SignupRequest> findRequestsAfterDate(@Param("date") java.time.LocalDateTime date);
}
