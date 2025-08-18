package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private Integer level;
    private Integer pointsTotal;
    private String college;
    private String campus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
