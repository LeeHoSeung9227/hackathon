package com.hackathon.dto;

import java.time.LocalDateTime;

public class PointHistoryDto {
    private Long id;
    private Long imagesId;
    private Long userId;
    private String changeType;
    private Integer amount;
    private String description;
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public PointHistoryDto() {}
    
    // 전체 생성자
    public PointHistoryDto(Long id, Long imagesId, Long userId, String changeType, 
                           Integer amount, String description, LocalDateTime createdAt) {
        this.id = id;
        this.imagesId = imagesId;
        this.userId = userId;
        this.changeType = changeType;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    // Getter와 Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getImagesId() {
        return imagesId;
    }
    
    public void setImagesId(Long imagesId) {
        this.imagesId = imagesId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getChangeType() {
        return changeType;
    }
    
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "PointHistoryDto{" +
                "id=" + id +
                ", imagesId=" + imagesId +
                ", userId=" + userId +
                ", changeType='" + changeType + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
