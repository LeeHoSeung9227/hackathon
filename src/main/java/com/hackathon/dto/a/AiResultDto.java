package com.hackathon.dto.a;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
