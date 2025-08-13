package com.hackathon.service;

import com.hackathon.dto.RankingDto;
import com.hackathon.entity.User;
import com.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final UserRepository userRepository;

    public List<RankingDto> getRankingsByCategory(String category) {
        List<User> users;
        
        switch (category.toUpperCase()) {
            case "INDIVIDUAL":
                users = userRepository.findAllByOrderByPointsDesc();
                break;
            case "COLLEGE":
                // 단과대별 랭킹 (간단한 구현)
                users = userRepository.findAllByOrderByPointsDesc();
                break;
            case "CAMPUS":
                // 캠퍼스별 랭킹 (간단한 구현)
                users = userRepository.findAllByOrderByPointsDesc();
                break;
            default:
                users = userRepository.findAllByOrderByPointsDesc();
        }
        
        return users.stream()
                .map(this::convertToRankingDto)
                .collect(Collectors.toList());
    }
    
    public RankingDto getUserRank(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        List<User> allUsers = userRepository.findAllByOrderByPointsDesc();
        int rank = 1;
        for (User u : allUsers) {
            if (u.getId().equals(userId)) {
                break;
            }
            rank++;
        }
        
        RankingDto dto = convertToRankingDto(user);
        dto.setRank(rank);
        return dto;
    }
    
    private RankingDto convertToRankingDto(User user) {
        RankingDto dto = new RankingDto();
        dto.setUserId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setCampus(user.getCampus());
        dto.setCollege(user.getCollege());
        dto.setPoints(user.getPoints());
        dto.setMembershipLevel(user.getMembershipLevel());
        return dto;
    }
}

