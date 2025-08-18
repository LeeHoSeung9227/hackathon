package com.hackathon.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activity")
@Data
public class DailyActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;
    
    @Column(name = "activity_count", columnDefinition = "integer default 0")
    private Integer activityCount = 0;
    
    @Column(name = "total_points", columnDefinition = "integer default 0")
    private Integer totalPoints = 0;
    
    @Column(name = "has_badge", columnDefinition = "boolean default false")
    private Boolean hasBadge = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
