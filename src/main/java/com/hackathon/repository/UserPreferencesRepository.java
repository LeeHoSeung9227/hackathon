package com.hackathon.repository;

import com.hackathon.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    
    List<UserPreferences> findByUserId(Long userId);
    
    Optional<UserPreferences> findByUserIdAndKey(Long userId, String key);
    
    List<UserPreferences> findByUserIdAndKeyIn(Long userId, List<String> keys);
    
    @Query("SELECT u FROM UserPreferences u WHERE u.userId = :userId AND (u.expiresAt IS NULL OR u.expiresAt > :now)")
    List<UserPreferences> findValidByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    void deleteByUserIdAndKey(Long userId, String key);
    
    void deleteByUserId(Long userId);
}
