package com.hackathon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "point_cost", nullable = false)
    private Integer pointCost;
    
    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive = true;
    
    @Column(name = "is_coupon_enabled", columnDefinition = "boolean default false")
    private Boolean isCouponEnabled = false;
    
    // 기존 필드들 (호환성을 위해 유지)
    private String description;
    private String category;
    private String imageUrl;
    private Integer stock;
    
    // 호환성을 위한 메서드들
    public String getName() {
        return this.productName;
    }
    
    public void setName(String name) {
        this.productName = name;
    }
    
    public java.math.BigDecimal getPrice() {
        return java.math.BigDecimal.valueOf(this.pointCost);
    }
    
    public void setPrice(java.math.BigDecimal price) {
        this.pointCost = price.intValue();
    }
    
    // 기본 생성자
    public Product() {}
    
    // Getter와 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public Integer getPointCost() { return pointCost; }
    public void setPointCost(Integer pointCost) { this.pointCost = pointCost; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsCouponEnabled() { return isCouponEnabled; }
    public void setIsCouponEnabled(Boolean isCouponEnabled) { this.isCouponEnabled = isCouponEnabled; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", pointCost=" + pointCost +
                ", isActive=" + isActive +
                ", isCouponEnabled=" + isCouponEnabled +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", stock=" + stock +
                '}';
    }
}
