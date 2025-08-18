package com.hackathon.service.a;

import com.hackathon.dto.a.ImageDto;
import com.hackathon.entity.a.Image;
import com.hackathon.repository.a.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    
    private final ImageRepository imageRepository;
    
    public ImageDto createImage(Long userId, String imageUrl, String fileName, String contentType, Long fileSize) {
        Image image = new Image();
        image.setUserId(userId);
        image.setImageUrl(imageUrl);
        image.setFileName(fileName);
        image.setContentType(contentType);
        image.setFileSize(fileSize);
        
        Image savedImage = imageRepository.save(image);
        return convertToDto(savedImage);
    }
    
    public ImageDto getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다: " + id));
        return convertToDto(image);
    }
    
    public List<ImageDto> getImagesByUserId(Long userId) {
        return imageRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ImageDto> getImagesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return imageRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ImageDto> getImagesByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return imageRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("이미지를 찾을 수 없습니다: " + id);
        }
        imageRepository.deleteById(id);
    }
    
    private ImageDto convertToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUserId(image.getUserId());
        dto.setImageUrl(image.getImageUrl());
        dto.setFileName(image.getFileName());
        dto.setContentType(image.getContentType());
        dto.setFileSize(image.getFileSize());
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        return dto;
    }
}
