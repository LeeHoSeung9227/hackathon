package com.hackathon.repository.b;

import com.hackathon.entity.b.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findByStatusOrderByCreatedAtDesc(String status);
    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}
