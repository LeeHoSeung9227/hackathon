package com.hackathon.repository.b;

import com.hackathon.entity.b.WeeklyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyActivityRepository extends JpaRepository<WeeklyActivity, Long> {
    Optional<WeeklyActivity> findByUserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);
    List<WeeklyActivity> findByUserIdOrderByWeekStartDateDesc(Long userId);
    List<WeeklyActivity> findByWeekStartDateOrderByTotalPointsDesc(LocalDate weekStartDate);
}
