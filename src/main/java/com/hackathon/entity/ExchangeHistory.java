package com.hackathon.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_history")
public class ExchangeHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "exchanged_at", nullable = false)
    private LocalDateTime exchangedAt;
    
    // 기본 생성자
    public ExchangeHistory() {}
    
    // 전체 생성자
    public ExchangeHistory(Long id, Long userId, Long productId, LocalDateTime exchangedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.exchangedAt = exchangedAt;
    }
    
    // Getter와 Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public LocalDateTime getExchangedAt() {
        return exchangedAt;
    }
    
    public void setExchangedAt(LocalDateTime exchangedAt) {
        this.exchangedAt = exchangedAt;
    }
    
    @Override
    public String toString() {
        return "ExchangeHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", exchangedAt=" + exchangedAt +
                '}';
    }
}
