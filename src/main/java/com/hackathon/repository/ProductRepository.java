package com.hackathon.repository;

import com.hackathon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByIsActiveTrue();
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findByPointCostBetween(Integer minPointCost, Integer maxPointCost);
}
