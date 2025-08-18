package com.hackathon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiResultDto {
    private Long id;
    private Long imagesId;
    private String materialType;
    private BigDecimal confidence;
    private Boolean isApproved;
    private LocalDateTime analyzedAt;
    
    // 기본 생성자
    public AiResultDto() {}
    
    // 전체 생성자
    public AiResultDto(Long id, Long imagesId, String materialType, BigDecimal confidence, 
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
        return "AiResultDto{" +
                "id=" + id +
                ", imagesId=" + imagesId +
                ", materialType='" + materialType + '\'' +
                ", confidence=" + confidence +
                ", isApproved=" + isApproved +
                ", analyzedAt=" + analyzedAt +
                '}';
    }
}
