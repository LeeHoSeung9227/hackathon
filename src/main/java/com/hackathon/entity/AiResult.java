package com.hackathon.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_results")
public class AiResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "images_id", nullable = false)
    private Long imagesId;
    
    @Column(name = "material_type", nullable = false)
    private String materialType;
    
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal confidence;
    
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;
    
    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;
    
    // 기본 생성자
    public AiResult() {}
    
    // 전체 생성자
    public AiResult(Long id, Long imagesId, String materialType, BigDecimal confidence, 
                    Boolean isApproved, LocalDateTime analyzedAt) {
        this.id = id;
        this.imagesId = imagesId;
        this.materialType = materialType;
        this.confidence = confidence;
        this.isApproved = isApproved;
        this.analyzedAt = analyzedAt;
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
    
    public String getMaterialType() {
        return materialType;
    }
    
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
    
    public BigDecimal getConfidence() {
        return confidence;
    }
    
    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }
    
    public Boolean getIsApproved() {
        return isApproved;
    }
    
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
    
    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }
    
    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
    
    @Override
    public String toString() {
        return "AiResult{" +
                "id=" + id +
                ", imagesId=" + imagesId +
                ", materialType='" + materialType + '\'' +
                ", confidence=" + confidence +
                ", isApproved=" + isApproved +
                ", analyzedAt=" + analyzedAt +
                '}';
    }
}
