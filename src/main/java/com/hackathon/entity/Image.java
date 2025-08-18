package com.hackathon.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;
    
    @Column(nullable = false)
    private String status;
    
    // 기본 생성자
    public Image() {}
    
    // 전체 생성자
    public Image(Long id, Long userId, String imageUrl, LocalDateTime capturedAt, String status) {
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
        return "Image{" +
                "id=" + id +
                ", userId=" + userId +
                ", imageUrl='" + imageUrl + '\'' +
                ", capturedAt=" + capturedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
