package com.hackathon.service.a;

import com.hackathon.dto.a.RankingDto;
import com.hackathon.entity.a.Ranking;
import com.hackathon.repository.a.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {
    
    private final RankingRepository rankingRepository;
    
    public RankingDto createRanking(Long userId, String username, String category, Integer rankPosition, Integer points, String scope) {
        Ranking ranking = new Ranking();
        ranking.setUserId(userId);
        ranking.setUsername(username);
        ranking.setCategory(category);
        ranking.setRankPosition(rankPosition);
        ranking.setPoints(points);
        ranking.setScope(scope);
        
        Ranking savedRanking = rankingRepository.save(ranking);
        return convertToDto(savedRanking);
    }
    
    public RankingDto getRankingById(Long id) {
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹을 찾을 수 없습니다: " + id));
        return convertToDto(ranking);
    }
    
    public List<RankingDto> getRankingsByCategory(String category) {
        return rankingRepository.findByCategoryOrderByPointsDesc(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<RankingDto> getRankingsByScope(String scope) {
        return rankingRepository.findByScopeOrderByPointsDesc(scope).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<RankingDto> getUserRankings(Long userId) {
        return rankingRepository.findByUserIdOrderByPointsDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<RankingDto> getRankingsByCategoryAndScope(String category, String scope) {
        return rankingRepository.findByCategoryAndScopeOrderByPointsDesc(category, scope).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public RankingDto updateRanking(Long id, Integer rankPosition, Integer points) {
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹을 찾을 수 없습니다: " + id));
        
        ranking.setRankPosition(rankPosition);
        ranking.setPoints(points);
        
        Ranking updatedRanking = rankingRepository.save(ranking);
        return convertToDto(updatedRanking);
    }
    
    public void deleteRanking(Long id) {
        if (!rankingRepository.existsById(id)) {
            throw new RuntimeException("랭킹을 찾을 수 없습니다: " + id);
        }
        rankingRepository.deleteById(id);
    }
    
    private RankingDto convertToDto(Ranking ranking) {
        RankingDto dto = new RankingDto();
        dto.setId(ranking.getId());
        dto.setUserId(ranking.getUserId());
        dto.setUsername(ranking.getUsername());
        dto.setCategory(ranking.getCategory());
        dto.setRankPosition(ranking.getRankPosition());
        dto.setPoints(ranking.getPoints());
        dto.setScope(ranking.getScope());
        dto.setCreatedAt(ranking.getCreatedAt());
        dto.setUpdatedAt(ranking.getUpdatedAt());
        return dto;
    }
}
