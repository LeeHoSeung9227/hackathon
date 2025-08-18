package com.hackathon.repository;

import com.hackathon.entity.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    
    Optional<DailyActivity> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);
    
    List<DailyActivity> findByUserId(Long userId);
    
    List<DailyActivity> findByUserIdAndActivityDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT d FROM DailyActivity d WHERE d.userId = :userId AND d.activityDate >= :startDate ORDER BY d.activityDate DESC")
    List<DailyActivity> findRecentByUserId(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);
    
    @Query("SELECT SUM(d.totalPoints) FROM DailyActivity d WHERE d.userId = :userId AND d.activityDate BETWEEN :startDate AND :endDate")
    Integer sumPointsByUserIdAndDateRange(@Param("userId") Long userId, 
                                          @Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
}
