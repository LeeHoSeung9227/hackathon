package com.hackathon.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_login")
public class AuthLogin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    // 기본 생성자
    public AuthLogin() {}
    
    // 전체 생성자
    public AuthLogin(Long id, Long userId, String token, LocalDateTime loginAt, Boolean isActive) {
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
        return "AuthLogin{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", loginAt=" + loginAt +
                ", isActive=" + isActive +
                '}';
    }
}
