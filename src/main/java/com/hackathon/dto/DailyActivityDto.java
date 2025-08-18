package com.hackathon.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyActivityDto {
    
    private Long id;
    private Long userId;
    private LocalDate activityDate;
    private Integer activityCount;
    private Integer totalPoints;
    private Boolean hasBadge;
    private LocalDateTime createdAt;
    
    // 추가 정보
    private String username;
    private String nickname;
    
    // 생성자
    public DailyActivityDto() {}
    
    public DailyActivityDto(Long userId, LocalDate activityDate) {
        this.userId = userId;
        this.activityDate = activityDate;
        this.activityCount = 0;
        this.totalPoints = 0;
        this.hasBadge = false;
        this.createdAt = LocalDateTime.now();
    }
}
