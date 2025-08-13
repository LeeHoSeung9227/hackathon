package com.hackathon.dto;

import lombok.Data;

@Data
public class RankingDto {
    private Long userId;
    private String nickname;
    private String campus;
    private String college;
    private Integer rank;
    private Integer points;
    private String membershipLevel;
}

