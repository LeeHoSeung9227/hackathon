package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AiResultDto {
    private Long id;
    private Long userId;
    private Long imageId;
    private String wasteType;
    private Double confidence;
    private String resultData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
