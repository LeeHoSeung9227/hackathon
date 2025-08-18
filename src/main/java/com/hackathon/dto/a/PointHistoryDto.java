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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
