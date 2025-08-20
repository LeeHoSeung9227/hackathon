package com.hackathon.repository.b;

import com.hackathon.entity.b.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long> {
    List<ExchangeHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ExchangeHistory> findByProductNameOrderByCreatedAtDesc(String productName);
    List<ExchangeHistory> findByUserIdAndProductNameOrderByCreatedAtDesc(Long userId, String productName);
}
