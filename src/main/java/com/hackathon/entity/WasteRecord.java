package com.hackathon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "waste_records")
@Data
public class WasteRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String wasteType; // PET, CAN, PAPER, etc.
    
    @Column(nullable = false)
    private Integer earnedPoints;
    
    @Column(nullable = false)
    private Integer accumulatedPoints;
    
    @Column(nullable = false)
    private LocalDateTime recordedAt;
    
    @Column(nullable = false)
    private String status = "SUCCESS"; // SUCCESS, FAILED
    
    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}

