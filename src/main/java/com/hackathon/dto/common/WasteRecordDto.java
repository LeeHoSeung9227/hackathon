package com.hackathon.dto.common;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WasteRecordDto {
    private Long id;
    private Long userId;
    private String wasteType;
    private Integer points;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
