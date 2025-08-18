package com.hackathon.dto.a;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignupRequestDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String college;
    private String campus;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
