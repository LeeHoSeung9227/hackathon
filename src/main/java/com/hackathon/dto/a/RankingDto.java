package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RankingDto {
    private Long id;
    private Long userId;
    private String username;
    private String category;
    private Integer rankPosition;
    private Integer points;
    private String scope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
