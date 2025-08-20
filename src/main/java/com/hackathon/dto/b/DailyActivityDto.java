package com.hackathon.dto.b;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyActivityDto {
    private Long id;
    private Long userId;
    private LocalDate activityDate;
    private Integer totalPoints;
    private Integer activitiesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
