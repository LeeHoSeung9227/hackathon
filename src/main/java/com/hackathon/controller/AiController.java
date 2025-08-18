package com.hackathon.controller;

import com.hackathon.dto.AiResultDto;
import com.hackathon.dto.ImageDto;
import com.hackathon.entity.AiResult;
import com.hackathon.entity.Image;
import com.hackathon.repository.AiResultRepository;
import com.hackathon.repository.ImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {
    
    private final AiResultRepository aiResultRepository;
    private final ImageRepository imageRepository;
    
    public AiController(AiResultRepository aiResultRepository, ImageRepository imageRepository) {
        this.aiResultRepository = aiResultRepository;
        this.imageRepository = imageRepository;
    }
    
    // ===== AI 분석 결과 =====
    
    /**
     * 이미지별 AI 분석 결과 조회
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getAiResultByImage(@PathVariable Long imagesId) {
        try {
            var aiResult = aiResultRepository.findByImagesId(imagesId);
            
            if (aiResult.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToDto(aiResult.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "AI 분석 결과를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 AI 분석 결과 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserAiResults(@PathVariable Long userId) {
        try {
            List<AiResult> aiResults = aiResultRepository.findByUserId(userId);
            
            List<AiResultDto> aiResultDtos = aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 재질 타입별 AI 분석 결과 조회
     */
    @GetMapping("/material/{materialType}")
    public ResponseEntity<Map<String, Object>> getAiResultsByMaterialType(@PathVariable String materialType) {
        try {
            List<AiResult> aiResults = aiResultRepository.findByMaterialType(materialType);
            
            List<AiResultDto> aiResultDtos = aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 승인 상태별 AI 분석 결과 조회
     */
    @GetMapping("/status/{isApproved}")
    public ResponseEntity<Map<String, Object>> getAiResultsByApprovalStatus(@PathVariable Boolean isApproved) {
        try {
            List<AiResult> aiResults = aiResultRepository.findByIsApproved(isApproved);
            
            List<AiResultDto> aiResultDtos = aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 AI 분석 결과 조회
     */
    @GetMapping("/range")
    public ResponseEntity<Map<String, Object>> getAiResultsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<AiResult> aiResults = aiResultRepository.findByCreatedAtBetween(start, end);
            
            List<AiResultDto> aiResultDtos = aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== 이미지 관리 =====
    
    /**
     * 사용자 이미지 목록 조회
     */
    @GetMapping("/images/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserImages(@PathVariable Long userId) {
        try {
            List<Image> images = imageRepository.findByUserId(userId);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 상태별 이미지 조회
     */
    @GetMapping("/images/status/{status}")
    public ResponseEntity<Map<String, Object>> getImagesByStatus(@PathVariable String status) {
        try {
            List<Image> images = imageRepository.findByStatus(status);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 상태별 이미지 조회
     */
    @GetMapping("/images/user/{userId}/status/{status}")
    public ResponseEntity<Map<String, Object>> getUserImagesByStatus(
            @PathVariable Long userId, 
            @PathVariable String status) {
        try {
            List<Image> images = imageRepository.findByUserIdAndStatus(userId, status);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 이미지 조회
     */
    @GetMapping("/images/range")
    public ResponseEntity<Map<String, Object>> getImagesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<Image> images = imageRepository.findByCreatedAtBetween(start, end);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 이미지 상세 정보 조회
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<Map<String, Object>> getImageById(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            
            if (image.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToImageDto(image.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "이미지를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private AiResultDto convertToDto(AiResult aiResult) {
        AiResultDto dto = new AiResultDto();
        dto.setId(aiResult.getId());
        dto.setImagesId(aiResult.getImagesId());
        dto.setUserId(aiResult.getUserId());
        dto.setMaterialType(aiResult.getMaterialType());
        dto.setConfidence(aiResult.getConfidence());
        dto.setIsApproved(aiResult.getIsApproved());
        dto.setCreatedAt(aiResult.getCreatedAt());
        return dto;
    }
    
    private ImageDto convertToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUserId(image.getUserId());
        dto.setImageUrl(image.getImageUrl());
        dto.setStatus(image.getStatus());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }
}
