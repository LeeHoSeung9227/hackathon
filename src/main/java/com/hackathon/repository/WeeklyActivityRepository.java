package com.hackathon.repository;

import com.hackathon.entity.WeeklyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyActivityRepository extends JpaRepository<WeeklyActivity, Long> {
    
    Optional<WeeklyActivity> findByUserIdAndYearWeek(Long userId, String yearWeek);
    
    List<WeeklyActivity> findByUserId(Long userId);
    
    List<WeeklyActivity> findByYearWeek(String yearWeek);
    
    @Query("SELECT w FROM WeeklyActivity w WHERE w.userId = :userId ORDER BY w.yearWeek DESC")
    List<WeeklyActivity> findByUserIdOrderByYearWeekDesc(@Param("userId") Long userId);
    
    @Query("SELECT w FROM WeeklyActivity w WHERE w.yearWeek = :yearWeek ORDER BY w.totalPoints DESC")
    List<WeeklyActivity> findTopByYearWeekOrderByPoints(@Param("yearWeek") String yearWeek);
}
