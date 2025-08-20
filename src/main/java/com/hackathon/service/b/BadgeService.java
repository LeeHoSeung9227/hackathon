package com.hackathon.service.b;

import com.hackathon.dto.b.BadgeDto;
import com.hackathon.entity.b.Badge;
import com.hackathon.repository.b.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService {
    
    private final BadgeRepository badgeRepository;
    
    public BadgeDto createBadge(String name, String description, String imageUrl, Integer pointsRequired, String category) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setImageUrl(imageUrl);
        badge.setPointsRequired(pointsRequired);
        badge.setCategory(category);
        
        Badge savedBadge = badgeRepository.save(badge);
        return convertToDto(savedBadge);
    }
    
    public BadgeDto getBadgeById(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("뱃지를 찾을 수 없습니다: " + id));
        return convertToDto(badge);
    }
    
    public List<BadgeDto> getAllBadges() {
        return badgeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BadgeDto> getBadgesByCategory(String category) {
        return badgeRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BadgeDto> getBadgesByPointsRequired(Integer maxPoints) {
        return badgeRepository.findByPointsRequiredLessThanEqual(maxPoints).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BadgeDto> getBadgesByCategoryAndPointsRequired(String category, Integer maxPoints) {
        return badgeRepository.findByCategoryAndPointsRequiredLessThanEqual(category, maxPoints).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BadgeDto updateBadge(Long id, BadgeDto badgeDto) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("뱃지를 찾을 수 없습니다: " + id));
        
        badge.setName(badgeDto.getName());
        badge.setDescription(badgeDto.getDescription());
        badge.setImageUrl(badgeDto.getImageUrl());
        badge.setPointsRequired(badgeDto.getPointsRequired());
        badge.setCategory(badgeDto.getCategory());
        
        Badge updatedBadge = badgeRepository.save(badge);
        return convertToDto(updatedBadge);
    }
    
    public void deleteBadge(Long id) {
        if (!badgeRepository.existsById(id)) {
            throw new RuntimeException("뱃지를 찾을 수 없습니다: " + id);
        }
        badgeRepository.deleteById(id);
    }
    
    private BadgeDto convertToDto(Badge badge) {
        BadgeDto dto = new BadgeDto();
        dto.setId(badge.getId());
        dto.setName(badge.getName());
        dto.setDescription(badge.getDescription());
        dto.setImageUrl(badge.getImageUrl());
        dto.setPointsRequired(badge.getPointsRequired());
        dto.setCategory(badge.getCategory());
        dto.setCreatedAt(badge.getCreatedAt());
        dto.setUpdatedAt(badge.getUpdatedAt());
        return dto;
    }
}
