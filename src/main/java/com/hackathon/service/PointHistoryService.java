package com.hackathon.service;

import com.hackathon.dto.PointHistoryDto;
import com.hackathon.entity.PointHistory;
import com.hackathon.repository.PointHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PointHistoryService {
    
    private final PointHistoryRepository pointHistoryRepository;
    
    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }
    
    /**
     * 새로운 포인트 내역 생성
     */
    public PointHistoryDto createPointHistory(Long imagesId, Long userId, String changeType, 
                                            Integer amount, String description) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.setImagesId(imagesId);
        pointHistory.setUserId(userId);
        pointHistory.setChangeType(changeType);
        pointHistory.setAmount(amount);
        pointHistory.setDescription(description);
        pointHistory.setCreatedAt(LocalDateTime.now());
        
        PointHistory saved = pointHistoryRepository.save(pointHistory);
        return convertToDto(saved);
    }
    
    /**
     * 포인트 내역 조회
     */
    public Optional<PointHistoryDto> getPointHistory(Long id) {
        return pointHistoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 포인트 내역 조회
     */
    public List<PointHistoryDto> getUserPointHistory(Long userId) {
        return pointHistoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 이미지별 포인트 내역 조회
     */
    public List<PointHistoryDto> getImagePointHistory(Long imagesId) {
        return pointHistoryRepository.findByImagesId(imagesId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 변경 타입별 포인트 내역 조회
     */
    public List<PointHistoryDto> getUserPointHistoryByType(Long userId, String changeType) {
        return pointHistoryRepository.findByUserIdAndChangeType(userId, changeType)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 날짜 범위별 포인트 내역 조회
     */
    public List<PointHistoryDto> getUserPointHistoryByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return pointHistoryRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 사용자 총 포인트 합계 조회
     */
    public Integer getUserTotalPoints(Long userId) {
        Integer totalPoints = pointHistoryRepository.sumAmountByUserId(userId);
        return totalPoints != null ? totalPoints : 0;
    }
    
    /**
     * 포인트 내역 삭제
     */
    public void deletePointHistory(Long id) {
        pointHistoryRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private PointHistoryDto convertToDto(PointHistory pointHistory) {
        PointHistoryDto dto = new PointHistoryDto();
        dto.setId(pointHistory.getId());
        dto.setImagesId(pointHistory.getImagesId());
        dto.setUserId(pointHistory.getUserId());
        dto.setChangeType(pointHistory.getChangeType());
        dto.setAmount(pointHistory.getAmount());
        dto.setDescription(pointHistory.getDescription());
        dto.setCreatedAt(pointHistory.getCreatedAt());
        return dto;
    }
}
