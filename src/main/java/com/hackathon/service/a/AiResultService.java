package com.hackathon.service.a;

import com.hackathon.dto.a.AiResultDto;
import com.hackathon.entity.a.AiResult;
import com.hackathon.repository.a.AiResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AiResultService {
    
    private final AiResultRepository aiResultRepository;
    
    public AiResultDto createAiResult(Long userId, Long imageId, String wasteType, Double confidence, String resultData) {
        AiResult result = new AiResult();
        result.setUserId(userId);
        result.setImageId(imageId);
        result.setWasteType(wasteType);
        result.setConfidence(confidence);
        result.setResultData(resultData);
        
        AiResult savedResult = aiResultRepository.save(result);
        return convertToDto(savedResult);
    }
    
    public AiResultDto getAiResultById(Long id) {
        AiResult result = aiResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AI 결과를 찾을 수 없습니다: " + id));
        return convertToDto(result);
    }
    
    public List<AiResultDto> getAiResultsByUserId(Long userId) {
        return aiResultRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AiResultDto> getAiResultsByImageId(Long imageId) {
        return aiResultRepository.findByImageIdOrderByCreatedAtDesc(imageId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AiResultDto> getAiResultsByWasteType(String wasteType) {
        return aiResultRepository.findByWasteTypeOrderByCreatedAtDesc(wasteType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AiResultDto> getAiResultsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return aiResultRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AiResultDto> getAiResultsByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return aiResultRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteAiResult(Long id) {
        if (!aiResultRepository.existsById(id)) {
            throw new RuntimeException("AI 결과를 찾을 수 없습니다: " + id);
        }
        aiResultRepository.deleteById(id);
    }
    
    private AiResultDto convertToDto(AiResult result) {
        AiResultDto dto = new AiResultDto();
        dto.setId(result.getId());
        dto.setUserId(result.getUserId());
        dto.setImageId(result.getImageId());
        dto.setWasteType(result.getWasteType());
        dto.setConfidence(result.getConfidence());
        dto.setResultData(result.getResultData());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        return dto;
    }
}
