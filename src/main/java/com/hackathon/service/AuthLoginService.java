package com.hackathon.service;

import com.hackathon.dto.AuthLoginDto;
import com.hackathon.entity.AuthLogin;
import com.hackathon.repository.AuthLoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthLoginService {
    
    private final AuthLoginRepository authLoginRepository;
    
    public AuthLoginService(AuthLoginRepository authLoginRepository) {
        this.authLoginRepository = authLoginRepository;
    }
    
    /**
     * 새로운 로그인 세션 생성
     */
    public AuthLoginDto createLoginSession(Long userId) {
        // 기존 세션이 있다면 삭제 (단일 세션 정책)
        authLoginRepository.deleteByUserId(userId);
        
        AuthLogin authLogin = new AuthLogin();
        authLogin.setUserId(userId);
        authLogin.setToken(generateToken());
        authLogin.setLoginAt(LocalDateTime.now());
        authLogin.setIsActive(true);
        
        AuthLogin saved = authLoginRepository.save(authLogin);
        return convertToDto(saved);
    }
    
    /**
     * 토큰으로 로그인 세션 조회
     */
    public Optional<AuthLoginDto> getLoginSession(String token) {
        return authLoginRepository.findByTokenAndIsActiveTrue(token)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 로그인 세션 조회
     */
    public List<AuthLoginDto> getLoginSessionsByUserId(Long userId) {
        return authLoginRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 로그인 세션 삭제 (로그아웃)
     */
    public void deleteLoginSession(String token) {
        Optional<AuthLogin> session = authLoginRepository.findByToken(token);
        if (session.isPresent()) {
            AuthLogin authLogin = session.get();
            authLogin.setIsActive(false);
            authLoginRepository.save(authLogin);
        }
    }
    
    /**
     * 사용자별 모든 세션 로그아웃
     */
    public void deleteAllLoginSessions(Long userId) {
        List<AuthLogin> sessions = authLoginRepository.findByUserIdAndIsActiveTrue(userId);
        sessions.forEach(session -> {
            session.setIsActive(false);
            authLoginRepository.save(session);
        });
    }
    
    /**
     * 만료된 세션 정리 (24시간 이상)
     */
    public void cleanupExpiredSessions() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(24);
        List<AuthLogin> expiredSessions = authLoginRepository.findExpiredSessions(cutoffDate);
        expiredSessions.forEach(session -> {
            session.setIsActive(false);
            authLoginRepository.save(session);
        });
    }
    
    /**
     * 토큰 생성
     */
    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * DTO 변환
     */
    private AuthLoginDto convertToDto(AuthLogin authLogin) {
        AuthLoginDto dto = new AuthLoginDto();
        dto.setId(authLogin.getId());
        dto.setUserId(authLogin.getUserId());
        dto.setToken(authLogin.getToken());
        dto.setLoginAt(authLogin.getLoginAt());
        dto.setIsActive(authLogin.getIsActive());
        return dto;
    }
}
