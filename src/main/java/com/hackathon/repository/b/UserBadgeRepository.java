package com.hackathon.repository.b;

import com.hackathon.entity.b.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUserIdOrderByEarnedAtDesc(Long userId);
    List<UserBadge> findByBadgeIdOrderByEarnedAtDesc(Long badgeId);
    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);
    
    List<UserBadge> findByUserId(Long userId);
    Optional<UserBadge> findByUserIdAndBadgeId(Long userId, Long badgeId);
}
