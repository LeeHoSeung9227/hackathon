package com.hackathon.repository.a;

import com.hackathon.entity.a.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long> {
    List<ExchangeHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ExchangeHistory> findByProductIdOrderByCreatedAtDesc(Long productId);
    List<ExchangeHistory> findByUserIdAndProductIdOrderByCreatedAtDesc(Long userId, Long productId);
}
