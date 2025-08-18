package com.hackathon.service;

import com.hackathon.dto.ExchangeHistoryDto;
import com.hackathon.entity.ExchangeHistory;
import com.hackathon.repository.ExchangeHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExchangeHistoryService {
    
    private final ExchangeHistoryRepository exchangeHistoryRepository;
    
    public ExchangeHistoryService(ExchangeHistoryRepository exchangeHistoryRepository) {
        this.exchangeHistoryRepository = exchangeHistoryRepository;
    }
    
    /**
     * 새로운 교환 내역 생성
     */
    public ExchangeHistoryDto createExchange(Long userId, Long productId) {
        ExchangeHistory exchange = new ExchangeHistory();
        exchange.setUserId(userId);
        exchange.setProductId(productId);
        exchange.setExchangedAt(LocalDateTime.now());
        
        ExchangeHistory saved = exchangeHistoryRepository.save(exchange);
        return convertToDto(saved);
    }
    
    /**
     * 교환 내역 조회
     */
    public Optional<ExchangeHistoryDto> getExchange(Long id) {
        return exchangeHistoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * 사용자별 교환 내역 조회
     */
    public List<ExchangeHistoryDto> getUserExchangeHistory(Long userId) {
        return exchangeHistoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 상품별 교환 내역 조회
     */
    public List<ExchangeHistoryDto> getProductExchangeHistory(Long productId) {
        return exchangeHistoryRepository.findByProductId(productId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 사용자별 상품별 교환 내역 조회
     */
    public List<ExchangeHistoryDto> getUserProductExchangeHistory(Long userId, Long productId) {
        return exchangeHistoryRepository.findByUserIdAndProductId(userId, productId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 날짜 범위별 교환 내역 조회
     */
    public List<ExchangeHistoryDto> getUserExchangeHistoryByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return exchangeHistoryRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 상품별 교환 횟수 조회
     */
    public Long getProductExchangeCount(Long productId) {
        return exchangeHistoryRepository.countByProductId(productId);
    }
    
    /**
     * 교환 내역 삭제
     */
    public void deleteExchange(Long id) {
        exchangeHistoryRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private ExchangeHistoryDto convertToDto(ExchangeHistory exchange) {
        ExchangeHistoryDto dto = new ExchangeHistoryDto();
        dto.setId(exchange.getId());
        dto.setUserId(exchange.getUserId());
        dto.setProductId(exchange.getProductId());
        dto.setExchangedAt(exchange.getExchangedAt());
        return dto;
    }
}
