package com.hackathon.dto.b;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BadgeDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer pointsRequired;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
