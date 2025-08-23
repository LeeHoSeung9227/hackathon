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
public class RankingDto {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String college;
    private String campus;
    private String category;
    private Integer rankPosition;
    private Integer points;
    private String scope;
    private String scopeType;
    private Integer rank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
