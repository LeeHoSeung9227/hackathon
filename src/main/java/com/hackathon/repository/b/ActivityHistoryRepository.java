package com.hackathon.repository.b;

import com.hackathon.entity.b.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
    List<ActivityHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ActivityHistory> findByActivityTypeOrderByCreatedAtDesc(String activityType);
    List<ActivityHistory> findByUserIdAndActivityTypeOrderByCreatedAtDesc(Long userId, String activityType);
}
