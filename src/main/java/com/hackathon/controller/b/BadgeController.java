package com.hackathon.controller.b;

import com.hackathon.dto.b.BadgeDto;
import com.hackathon.entity.b.Badge;
import com.hackathon.repository.b.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeRepository badgeRepository;

    /**
     * 모든 뱃지 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBadges() {
        try {
            List<Badge> badges = badgeRepository.findAll();
            
            List<BadgeDto> badgeDtos = badges.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", badgeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 뱃지 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBadgeById(@PathVariable Long id) {
        try {
            var badge = badgeRepository.findById(id);
            
            if (badge.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToDto(badge.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "뱃지를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 카테고리별 뱃지 조회
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getBadgesByCategory(@PathVariable String category) {
        try {
            List<Badge> badges = badgeRepository.findByCategory(category);
            
            List<BadgeDto> badgeDtos = badges.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", badgeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 포인트 요구사항별 뱃지 조회
     */
    @GetMapping("/points/{requiredPoints}")
    public ResponseEntity<Map<String, Object>> getBadgesByRequiredPoints(@PathVariable Integer requiredPoints) {
        try {
            List<Badge> badges = badgeRepository.findByPointsRequiredLessThanEqual(requiredPoints);
            
            List<BadgeDto> badgeDtos = badges.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", badgeDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 새로운 뱃지 생성
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBadge(@RequestBody BadgeDto badgeDto) {
        try {
            Badge badge = new Badge();
            badge.setName(badgeDto.getName());
            badge.setDescription(badgeDto.getDescription());
            badge.setImageUrl(badgeDto.getImageUrl());
            badge.setPointsRequired(badgeDto.getPointsRequired());
            badge.setCategory(badgeDto.getCategory());
            
            Badge saved = badgeRepository.save(badge);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "뱃지가 성공적으로 생성되었습니다.",
                "data", convertToDto(saved)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 뱃지 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBadge(@PathVariable Long id, @RequestBody BadgeDto badgeDto) {
        try {
            var badge = badgeRepository.findById(id);
            
            if (badge.isPresent()) {
                Badge existingBadge = badge.get();
                existingBadge.setName(badgeDto.getName());
                existingBadge.setDescription(badgeDto.getDescription());
                existingBadge.setImageUrl(badgeDto.getImageUrl());
                existingBadge.setPointsRequired(badgeDto.getPointsRequired());
                existingBadge.setCategory(badgeDto.getCategory());
                
                Badge saved = badgeRepository.save(existingBadge);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "뱃지가 성공적으로 수정되었습니다.",
                    "data", convertToDto(saved)
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "뱃지를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 뱃지 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBadge(@PathVariable Long id) {
        try {
            var badge = badgeRepository.findById(id);
            
            if (badge.isPresent()) {
                badgeRepository.deleteById(id);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "뱃지가 성공적으로 삭제되었습니다."
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "뱃지를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private BadgeDto convertToDto(Badge badge) {
        BadgeDto dto = new BadgeDto();
        dto.setId(badge.getId());
        dto.setName(badge.getName());
        dto.setDescription(badge.getDescription());
        dto.setImageUrl(badge.getImageUrl());
        dto.setPointsRequired(badge.getPointsRequired());
        dto.setCategory(badge.getCategory());
        return dto;
    }
}
