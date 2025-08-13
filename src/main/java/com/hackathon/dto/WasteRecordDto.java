package com.hackathon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WasteRecordDto {
    private Long id;
    private Long userId;
    private String wasteType;
    private Integer earnedPoints;
    private Integer accumulatedPoints;
    private LocalDateTime recordedAt;
    private String status;
    private String userNickname;
}

