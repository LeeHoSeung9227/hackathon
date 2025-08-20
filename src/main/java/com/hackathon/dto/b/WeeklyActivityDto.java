package com.hackathon.dto.b;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WeeklyActivityDto {
    private Long id;
    private Long userId;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private Integer totalPoints;
    private Integer activitiesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
