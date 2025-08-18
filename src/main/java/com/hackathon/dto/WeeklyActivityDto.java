package com.hackathon.dto;

import lombok.Data;

@Data
public class WeeklyActivityDto {
    
    private Long id;
    private Long userId;
    private String yearWeek;
    private Integer activityCount;
    private Integer totalPoints;
    private Integer badgesCount;
    
    // 추가 정보
    private String username;
    private String nickname;
    
    // 생성자
    public WeeklyActivityDto() {}
    
    public WeeklyActivityDto(Long userId, String yearWeek) {
        this.userId = userId;
        this.yearWeek = yearWeek;
        this.activityCount = 0;
        this.totalPoints = 0;
        this.badgesCount = 0;
    }
}
