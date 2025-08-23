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
public class ImageDto {
    private Long id;
    private Long userId;
    private String imageUrl;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private byte[] imageData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
