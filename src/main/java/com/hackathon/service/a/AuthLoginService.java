package com.hackathon.service.a;

import com.hackathon.dto.a.AuthLoginDto;
import com.hackathon.entity.a.AuthLogin;
import com.hackathon.repository.a.AuthLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthLoginService {
    
    private final AuthLoginRepository authLoginRepository;
    
    public AuthLoginDto createLoginSession(Long userId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        return createLoginSession(userId, token, expiresAt);
    }

    public AuthLoginDto createLoginSession(Long userId, String token, LocalDateTime expiresAt) {
        AuthLogin authLogin = new AuthLogin();
        authLogin.setUserId(userId);
        authLogin.setToken(token);
        authLogin.setExpiresAt(expiresAt);
        
        AuthLogin savedSession = authLoginRepository.save(authLogin);
        return convertToDto(savedSession);
    }
    
    public AuthLoginDto getLoginSession(String token) {
        AuthLogin authLogin = authLoginRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("로그인 세션을 찾을 수 없습니다"));
        return convertToDto(authLogin);
    }
    
    public AuthLoginDto getLoginSessionByUserId(Long userId) {
        AuthLogin authLogin = authLoginRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("로그인 세션을 찾을 수 없습니다"));
        return convertToDto(authLogin);
    }
    
    public List<AuthLoginDto> getAllLoginSessions() {
        return authLoginRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteLoginSession(String token) {
        AuthLogin authLogin = authLoginRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("로그인 세션을 찾을 수 없습니다"));
        authLoginRepository.delete(authLogin);
    }
    
    public void deleteLoginSessionByUserId(Long userId) {
        authLoginRepository.deleteByUserId(userId);
    }
    
    private AuthLoginDto convertToDto(AuthLogin authLogin) {
        AuthLoginDto dto = new AuthLoginDto();
        dto.setId(authLogin.getId());
        dto.setUserId(authLogin.getUserId());
        dto.setToken(authLogin.getToken());
        dto.setExpiresAt(authLogin.getExpiresAt());
        dto.setCreatedAt(authLogin.getCreatedAt());
        dto.setUpdatedAt(authLogin.getUpdatedAt());
        return dto;
    }
}
