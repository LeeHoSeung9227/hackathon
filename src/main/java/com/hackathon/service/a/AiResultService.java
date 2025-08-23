package com.hackathon.service.a;

import com.hackathon.dto.a.AiResultDto;
import com.hackathon.entity.a.AiResult;
import com.hackathon.repository.a.AiResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiResultService {
    
    private final AiResultRepository aiResultRepository;
    
    /**
     * 이미지별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByImageId(Long imageId) {
        log.info("이미지 ID {}의 AI 분석 결과 조회", imageId);
        List<AiResult> aiResults = aiResultRepository.findByImageIdOrderByCreatedAtDesc(imageId);
        return aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByUserId(Long userId) {
        log.info("사용자 ID {}의 AI 분석 결과 조회", userId);
        List<AiResult> aiResults = aiResultRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 재질 타입별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByWasteType(String wasteType) {
        log.info("재질 타입 {}의 AI 분석 결과 조회", wasteType);
        List<AiResult> aiResults = aiResultRepository.findByWasteTypeOrderByCreatedAtDesc(wasteType);
        return aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 기간별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("기간 {} ~ {}의 AI 분석 결과 조회", startDate, endDate);
        List<AiResult> aiResults = aiResultRepository.findByCreatedAtBetween(startDate, endDate);
        return aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자별 기간별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("사용자 ID {}의 기간 {} ~ {} AI 분석 결과 조회", userId, startDate, endDate);
        List<AiResult> aiResults = aiResultRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate);
        return aiResults.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * AI 분석 결과 저장
     */
    @Transactional
    public AiResult saveAiResult(AiResult aiResult) {
        log.info("AI 분석 결과 저장: {}", aiResult);
        return aiResultRepository.save(aiResult);
    }
    
    /**
     * Entity를 DTO로 변환
     */
    private AiResultDto convertToDto(AiResult aiResult) {
        return AiResultDto.builder()
                .id(aiResult.getId())
                .userId(aiResult.getUserId())
                .imageId(aiResult.getImageId())
                .wasteType(aiResult.getWasteType())
                .confidence(aiResult.getConfidence())
                .resultData(aiResult.getResultData())
                .createdAt(aiResult.getCreatedAt())
                .updatedAt(aiResult.getUpdatedAt())
                .build();
    }
}
