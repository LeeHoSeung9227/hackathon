package com.hackathon.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPreferencesDto {
    
    private Long id;
    private Long userId;
    private String key;
    private String value;
    private LocalDateTime expiresAt;
    
    // 생성자
    public UserPreferencesDto() {}
    
    public UserPreferencesDto(Long userId, String key, String value) {
        this.userId = userId;
        this.key = key;
        this.value = value;
    }
    
    public UserPreferencesDto(Long userId, String key, String value, LocalDateTime expiresAt) {
        this.userId = userId;
        this.key = key;
        this.value = value;
        this.expiresAt = expiresAt;
    }
}
