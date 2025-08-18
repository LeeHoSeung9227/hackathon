package com.hackathon.service.a;

import com.hackathon.dto.a.PointHistoryDto;
import com.hackathon.entity.a.PointHistory;
import com.hackathon.repository.a.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryService {
    
    private final PointHistoryRepository pointHistoryRepository;
    
    public PointHistoryDto createPointHistory(Long userId, String type, Integer points, String description) {
        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setType(type);
        history.setPoints(points);
        history.setDescription(description);
        
        PointHistory savedHistory = pointHistoryRepository.save(history);
        return convertToDto(savedHistory);
    }
    
    public PointHistoryDto getPointHistoryById(Long id) {
        PointHistory history = pointHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("포인트 내역을 찾을 수 없습니다: " + id));
        return convertToDto(history);
    }
    
    public List<PointHistoryDto> getPointHistoryByUserId(Long userId) {
        return pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PointHistoryDto> getPointHistoryByUserIdAndType(Long userId, String type) {
        return pointHistoryRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PointHistoryDto> getPointHistoryByType(String type) {
        return pointHistoryRepository.findByTypeOrderByCreatedAtDesc(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deletePointHistory(Long id) {
        if (!pointHistoryRepository.existsById(id)) {
            throw new RuntimeException("포인트 내역을 찾을 수 없습니다: " + id);
        }
        pointHistoryRepository.deleteById(id);
    }
    
    private PointHistoryDto convertToDto(PointHistory history) {
        PointHistoryDto dto = new PointHistoryDto();
        dto.setId(history.getId());
        dto.setUserId(history.getUserId());
        dto.setType(history.getType());
        dto.setPoints(history.getPoints());
        dto.setDescription(history.getDescription());
        dto.setCreatedAt(history.getCreatedAt());
        dto.setUpdatedAt(history.getUpdatedAt());
        return dto;
    }
}
