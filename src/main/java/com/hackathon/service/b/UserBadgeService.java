package com.hackathon.service.b;

import com.hackathon.dto.b.BadgeDto;
import com.hackathon.entity.b.UserBadge;
import com.hackathon.repository.b.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBadgeService {
    
    private final UserBadgeRepository userBadgeRepository;
    
    public UserBadge createUserBadge(Long userId, Long badgeId) {
        UserBadge userBadge = new UserBadge();
        userBadge.setUserId(userId);
        userBadge.setBadgeId(badgeId);
        userBadge.setEarnedAt(LocalDateTime.now());
        
        UserBadge savedUserBadge = userBadgeRepository.save(userBadge);
        return savedUserBadge;
    }
    
    public UserBadge getUserBadgeById(Long id) {
        return userBadgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 뱃지를 찾을 수 없습니다: " + id));
    }
    
    public List<UserBadge> getAllUserBadges() {
        return userBadgeRepository.findAll();
    }
    
    public List<UserBadge> getUserBadgesByUserId(Long userId) {
        return userBadgeRepository.findByUserIdOrderByEarnedAtDesc(userId);
    }
    
    public List<UserBadge> getUserBadgesByBadgeId(Long badgeId) {
        return userBadgeRepository.findByBadgeIdOrderByEarnedAtDesc(badgeId);
    }
    
    public boolean hasUserBadge(Long userId, Long badgeId) {
        return userBadgeRepository.existsByUserIdAndBadgeId(userId, badgeId);
    }
    
    public void deleteUserBadge(Long id) {
        if (!userBadgeRepository.existsById(id)) {
            throw new RuntimeException("사용자 뱃지를 찾을 수 없습니다: " + id);
        }
        userBadgeRepository.deleteById(id);
    }
}
