package com.hackathon.repository.a;

import com.hackathon.entity.a.AiResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiResultRepository extends JpaRepository<AiResult, Long> {
    List<AiResult> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<AiResult> findByImageIdOrderByCreatedAtDesc(Long imageId);
    List<AiResult> findByWasteTypeOrderByCreatedAtDesc(String wasteType);
    List<AiResult> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<AiResult> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
