package com.hackathon.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    
    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;
    
    @NotNull(message = "주문 항목은 필수입니다")
    private List<OrderItemDto> orderItems;
    
    @NotNull(message = "총 금액은 필수입니다")
    @Positive(message = "총 금액은 양수여야 합니다")
    private BigDecimal totalAmount;
    
    private String status;
    private String shippingAddress;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private LocalDateTime updatedDate;
    
    @Data
    public static class OrderItemDto {
        @NotNull(message = "상품 ID는 필수입니다")
        private Long productId;
        
        @NotNull(message = "수량은 필수입니다")
        @Positive(message = "수량은 양수여야 합니다")
        private Integer quantity;
        
        @NotNull(message = "가격은 필수입니다")
        @Positive(message = "가격은 양수여야 합니다")
        private BigDecimal price;
    }
}
