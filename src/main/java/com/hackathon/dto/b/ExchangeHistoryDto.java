package com.hackathon.dto.b;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExchangeHistoryDto {
    private Long id;
    private Long userId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
