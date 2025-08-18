package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPreferencesDto {
    private Long id;
    private Long userId;
    private String preferenceType;
    private String preferenceValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
