package com.hackathon.service;

import com.hackathon.dto.AiResultDto;
import com.hackathon.entity.AiResult;
import com.hackathon.repository.AiResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AiResultService {
    
    private final AiResultRepository aiResultRepository;
    
    public AiResultService(AiResultRepository aiResultRepository) {
        this.aiResultRepository = aiResultRepository;
    }
    
    /**
     * 새로운 AI 분석 결과 등록
     */
    public AiResultDto createAiResult(Long imagesId, String materialType, BigDecimal confidence) {
        AiResult aiResult = new AiResult();
        aiResult.setImagesId(imagesId);
        aiResult.setMaterialType(materialType);
        aiResult.setConfidence(confidence);
        aiResult.setIsApproved(false);
        aiResult.setAnalyzedAt(LocalDateTime.now());
        
        AiResult saved = aiResultRepository.save(aiResult);
        return convertToDto(saved);
    }
    
    /**
     * AI 분석 결과 조회
     */
    public Optional<AiResultDto> getAiResult(Long id) {
        return aiResultRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * 이미지별 AI 분석 결과 조회
     */
    public Optional<AiResultDto> getAiResultByImage(Long imagesId) {
        return aiResultRepository.findByImagesId(imagesId)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 AI 분석 결과 조회
     */
    public List<AiResultDto> getUserAiResults(Long userId) {
        return aiResultRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 재질 타입별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByMaterialType(String materialType) {
        return aiResultRepository.findByMaterialType(materialType)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 승인 상태별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByApprovalStatus(Boolean isApproved) {
        return aiResultRepository.findByIsApproved(isApproved)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 날짜 범위별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return aiResultRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 최소 신뢰도별 AI 분석 결과 조회
     */
    public List<AiResultDto> getAiResultsByMinConfidence(Double minConfidence) {
        return aiResultRepository.findByMinConfidence(minConfidence)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * AI 분석 결과 승인 상태 업데이트
     */
    public AiResultDto updateAiResultApproval(Long id, Boolean isApproved) {
        AiResult aiResult = aiResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AI 분석 결과를 찾을 수 없습니다."));
        
        aiResult.setIsApproved(isApproved);
        AiResult saved = aiResultRepository.save(aiResult);
        return convertToDto(saved);
    }
    
    /**
     * AI 분석 결과 삭제
     */
    public void deleteAiResult(Long id) {
        aiResultRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private AiResultDto convertToDto(AiResult aiResult) {
        AiResultDto dto = new AiResultDto();
        dto.setId(aiResult.getId());
        dto.setImagesId(aiResult.getImagesId());
        dto.setMaterialType(aiResult.getMaterialType());
        dto.setConfidence(aiResult.getConfidence());
        dto.setIsApproved(aiResult.getIsApproved());
        dto.setAnalyzedAt(aiResult.getAnalyzedAt());
        return dto;
    }
}
