package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PointHistoryDto {
    private Long id;
    private Long userId;
    private String type;
    private Integer points;
    private String description;
    private Long imageId;  // 이미지 ID 추가
    private String wasteType;  // AI 분석 결과 wasteType 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
