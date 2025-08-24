package com.hackathon.dto.a;

import lombok.Builder;

@Builder
public class ImageAnalysisResult {
    private String classification;    // "PET-순수함", "CAN-이물질있음" 등
    private String reason;           // "라벨제거됨", "음료잔여물" 등
    private int pointsEarned;        // 0 또는 해당 타입별 포인트
    private String wasteType;        // PET, CAN, PAPER, 일반쓰레기
    private boolean isRecyclable;    // 분리수거 가능 여부
    
    // 수동으로 getter 메서드 추가
    public String getClassification() { return classification; }
    public String getReason() { return reason; }
    public int getPointsEarned() { return pointsEarned; }
    public String getWasteType() { return wasteType; }
    public boolean getIsRecyclable() { return isRecyclable; }
    
    // 수동으로 setter 메서드 추가
    public void setClassification(String classification) { this.classification = classification; }
    public void setReason(String reason) { this.reason = reason; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }
    public void setIsRecyclable(boolean isRecyclable) { this.isRecyclable = isRecyclable; }
}
