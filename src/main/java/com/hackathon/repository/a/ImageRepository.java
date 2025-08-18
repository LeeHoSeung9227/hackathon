package com.hackathon.repository.a;

import com.hackathon.entity.a.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Image> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Image> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
