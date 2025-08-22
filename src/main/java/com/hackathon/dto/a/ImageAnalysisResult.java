package com.hackathon.dto.a;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageAnalysisResult {
    private String classification;    // "PET-순수함", "CAN-이물질있음" 등
    private String reason;           // "라벨제거됨", "음료잔여물" 등
    private int pointsEarned;        // 0 또는 해당 타입별 포인트
    private String wasteType;        // PET, CAN, PAPER, 일반쓰레기
    private boolean isRecyclable;    // 분리수거 가능 여부
}
