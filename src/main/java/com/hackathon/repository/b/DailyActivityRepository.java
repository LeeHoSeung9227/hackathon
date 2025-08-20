package com.hackathon.repository.b;

import com.hackathon.entity.b.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    Optional<DailyActivity> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);
    List<DailyActivity> findByUserIdOrderByActivityDateDesc(Long userId);
    List<DailyActivity> findByActivityDateOrderByTotalPointsDesc(LocalDate activityDate);
}
