package com.hackathon.dto;

import java.time.LocalDateTime;

public class ImageDto {
    private Long id;
    private Long userId;
    private String imageUrl;
    private LocalDateTime capturedAt;
    private String status;
    
    // 기본 생성자
    public ImageDto() {}
    
    // 전체 생성자
    public ImageDto(Long id, Long userId, String imageUrl, LocalDateTime capturedAt, String status) {
        this.id = id;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.capturedAt = capturedAt;
        this.status = status;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }
    
    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "ImageDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", imageUrl='" + imageUrl + '\'' +
                ", capturedAt=" + capturedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
