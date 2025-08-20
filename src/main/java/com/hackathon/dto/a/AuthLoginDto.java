package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuthLoginDto {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
