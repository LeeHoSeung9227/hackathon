package com.hackathon.repository.b;

import com.hackathon.entity.b.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPointsRequiredBetween(Integer minPoints, Integer maxPoints);
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
}
