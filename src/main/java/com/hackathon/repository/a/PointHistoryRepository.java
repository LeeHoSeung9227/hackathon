package com.hackathon.repository.a;

import com.hackathon.entity.a.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByUserId(Long userId);
    List<PointHistory> findByUserIdAndType(Long userId, String type);
    List<PointHistory> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    List<PointHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<PointHistory> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);
    List<PointHistory> findByTypeOrderByCreatedAtDesc(String type);
    List<PointHistory> findByImageId(Long imageId);  // 이미지별 조회 추가
}
