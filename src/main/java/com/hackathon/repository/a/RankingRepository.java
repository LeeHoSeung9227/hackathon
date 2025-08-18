package com.hackathon.repository.a;

import com.hackathon.entity.a.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByCategoryOrderByPointsDesc(String category);
    List<Ranking> findByScopeOrderByPointsDesc(String scope);
    List<Ranking> findByUserIdOrderByPointsDesc(Long userId);
    List<Ranking> findByCategoryAndScopeOrderByPointsDesc(String category, String scope);
}
