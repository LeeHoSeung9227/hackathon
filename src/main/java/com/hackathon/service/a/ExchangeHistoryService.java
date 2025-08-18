package com.hackathon.service.a;

import com.hackathon.dto.a.ExchangeHistoryDto;
import com.hackathon.entity.a.ExchangeHistory;
import com.hackathon.repository.a.ExchangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeHistoryService {
    
    private final ExchangeHistoryRepository exchangeHistoryRepository;
    
    public ExchangeHistoryDto createExchangeHistory(Long userId, Long productId, String productName, 
                                                  Integer quantity, BigDecimal totalAmount, Integer pointsUsed) {
        ExchangeHistory history = new ExchangeHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        history.setProductName(productName);
        history.setQuantity(quantity);
        history.setTotalAmount(totalAmount);
        history.setPointsUsed(pointsUsed);
        
        ExchangeHistory savedHistory = exchangeHistoryRepository.save(history);
        return convertToDto(savedHistory);
    }
    
    public ExchangeHistoryDto getExchangeHistoryById(Long id) {
        ExchangeHistory history = exchangeHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 내역을 찾을 수 없습니다: " + id));
        return convertToDto(history);
    }
    
    public List<ExchangeHistoryDto> getExchangeHistoryByUserId(Long userId) {
        return exchangeHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ExchangeHistoryDto> getExchangeHistoryByProductId(Long productId) {
        return exchangeHistoryRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ExchangeHistoryDto> getExchangeHistoryByUserIdAndProductId(Long userId, Long productId) {
        return exchangeHistoryRepository.findByUserIdAndProductIdOrderByCreatedAtDesc(userId, productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteExchangeHistory(Long id) {
        if (!exchangeHistoryRepository.existsById(id)) {
            throw new RuntimeException("교환 내역을 찾을 수 없습니다: " + id);
        }
        exchangeHistoryRepository.deleteById(id);
    }
    
    private ExchangeHistoryDto convertToDto(ExchangeHistory history) {
        ExchangeHistoryDto dto = new ExchangeHistoryDto();
        dto.setId(history.getId());
        dto.setUserId(history.getUserId());
        dto.setProductId(history.getProductId());
        dto.setProductName(history.getProductName());
        dto.setQuantity(history.getQuantity());
        dto.setTotalAmount(history.getTotalAmount());
        dto.setPointsUsed(history.getPointsUsed());
        dto.setCreatedAt(history.getCreatedAt());
        dto.setUpdatedAt(history.getUpdatedAt());
        return dto;
    }
}
