package com.hackathon.service;

import com.hackathon.dto.ImageDto;
import com.hackathon.entity.Image;
import com.hackathon.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageService {
    
    private final ImageRepository imageRepository;
    
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    
    /**
     * 새로운 이미지 등록
     */
    public ImageDto uploadImage(Long userId, String imageUrl, String status) {
        Image image = new Image();
        image.setUserId(userId);
        image.setImageUrl(imageUrl);
        image.setStatus(status != null ? status : "pending");
        image.setCapturedAt(LocalDateTime.now());
        
        Image saved = imageRepository.save(image);
        return convertToDto(saved);
    }
    
    /**
     * 이미지 조회
     */
    public Optional<ImageDto> getImage(Long id) {
        return imageRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 이미지 목록 조회
     */
    public List<ImageDto> getUserImages(Long userId) {
        return imageRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 상태별 이미지 조회
     */
    public List<ImageDto> getImagesByStatus(String status) {
        return imageRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 사용자별 상태별 이미지 조회
     */
    public List<ImageDto> getUserImagesByStatus(Long userId, String status) {
        return imageRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 날짜 범위별 이미지 조회
     */
    public List<ImageDto> getImagesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return imageRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 사용자별 날짜 범위별 이미지 조회
     */
    public List<ImageDto> getUserImagesByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return imageRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 이미지 상태 업데이트
     */
    public ImageDto updateImageStatus(Long id, String status) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));
        
        image.setStatus(status);
        Image saved = imageRepository.save(image);
        return convertToDto(saved);
    }
    
    /**
     * 이미지 삭제
     */
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private ImageDto convertToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUserId(image.getUserId());
        dto.setImageUrl(image.getImageUrl());
        dto.setCapturedAt(image.getCapturedAt());
        dto.setStatus(image.getStatus());
        return dto;
    }
}
