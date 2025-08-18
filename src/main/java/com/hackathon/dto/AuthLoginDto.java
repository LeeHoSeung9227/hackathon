package com.hackathon.dto;

import java.time.LocalDateTime;

public class AuthLoginDto {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime loginAt;
    private Boolean isActive;
    
    // 기본 생성자
    public AuthLoginDto() {}
    
    // 전체 생성자
    public AuthLoginDto(Long id, Long userId, String token, LocalDateTime loginAt, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.loginAt = loginAt;
        this.isActive = isActive;
    }
    
    // Getter와 Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getLoginAt() {
        return loginAt;
    }
    
    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "AuthLoginDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", loginAt=" + loginAt +
                ", isActive=" + isActive +
                '}';
    }
}
