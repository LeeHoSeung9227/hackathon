package com.hackathon.repository.a;

import com.hackathon.entity.a.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<PointHistory> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);
    List<PointHistory> findByTypeOrderByCreatedAtDesc(String type);
}
