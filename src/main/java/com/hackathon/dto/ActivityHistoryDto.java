package com.hackathon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ActivityHistoryDto {
    private Long id;
    private Long userId;
    private LocalDate activityDate;
    private String activityType;
    private String activityName;
    private Integer points;
    private Long badgeId;
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public ActivityHistoryDto() {}
    
    // 전체 생성자
    public ActivityHistoryDto(Long id, Long userId, LocalDate activityDate, String activityType, 
                             String activityName, Integer points, Long badgeId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.activityDate = activityDate;
        this.activityType = activityType;
        this.activityName = activityName;
        this.points = points;
        this.badgeId = badgeId;
        this.createdAt = createdAt;
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
    
    public LocalDate getActivityDate() {
        return activityDate;
    }
    
    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public String getActivityName() {
        return activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public Long getBadgeId() {
        return badgeId;
    }
    
    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "ActivityHistoryDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityDate=" + activityDate +
                ", activityType='" + activityType + '\'' +
                ", activityName='" + activityName + '\'' +
                ", points=" + points +
                ", badgeId=" + badgeId +
                ", createdAt=" + createdAt +
                '}';
    }
}
