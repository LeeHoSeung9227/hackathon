package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ImageDto {
    private Long id;
    private Long userId;
    private String imageUrl;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
