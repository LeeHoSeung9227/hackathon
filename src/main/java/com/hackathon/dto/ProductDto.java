package com.hackathon.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    
    @NotBlank(message = "상품명은 필수입니다")
    @Size(min = 2, max = 100, message = "상품명은 2-100자 사이여야 합니다")
    private String name;
    
    @NotBlank(message = "상품 설명은 필수입니다")
    @Size(max = 500, message = "상품 설명은 500자 이하여야 합니다")
    private String description;
    
    @NotNull(message = "가격은 필수입니다")
    @Positive(message = "가격은 양수여야 합니다")
    private BigDecimal price;
    
    @NotNull(message = "재고는 필수입니다")
    @Positive(message = "재고는 양수여야 합니다")
    private Integer stock;
    
    private String category;
    private String imageUrl;
    private Boolean isActive;
}
