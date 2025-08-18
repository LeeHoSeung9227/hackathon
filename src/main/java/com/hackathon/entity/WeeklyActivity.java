package com.hackathon.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "weekly_activity")
@Data
public class WeeklyActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "year_week", nullable = false, length = 10)
    private String yearWeek;
    
    @Column(name = "activity_count")
    private Integer activityCount;
    
    @Column(name = "total_points")
    private Integer totalPoints;
    
    @Column(name = "badges_count")
    private Integer badgesCount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
