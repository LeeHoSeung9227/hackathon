package com.hackathon.repository.b;

import com.hackathon.entity.b.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByCategory(String category);
    List<Badge> findByPointsRequiredLessThanEqual(Integer points);
    List<Badge> findByCategoryAndPointsRequiredLessThanEqual(String category, Integer points);
}
