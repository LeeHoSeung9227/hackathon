package com.hackathon.dto.a;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExchangeHistoryDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer pointsUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
