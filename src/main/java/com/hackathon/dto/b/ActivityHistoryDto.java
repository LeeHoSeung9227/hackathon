package com.hackathon.dto.b;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityHistoryDto {
    private Long id;
    private Long userId;
    private String activityType;
    private String description;
    private Integer pointsEarned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
