package com.hackathon.service.b;

import com.hackathon.dto.b.ExchangeHistoryDto;
import com.hackathon.entity.b.ExchangeHistory;
import com.hackathon.repository.b.ExchangeHistoryRepository;
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

    public ExchangeHistoryDto createExchangeHistory(Long userId, String productName, Integer quantity, BigDecimal totalAmount) {
        ExchangeHistory exchange = new ExchangeHistory();
        exchange.setUserId(userId);
        exchange.setProductName(productName);
        exchange.setQuantity(quantity);
        exchange.setTotalAmount(totalAmount);

        ExchangeHistory savedExchange = exchangeHistoryRepository.save(exchange);
        return convertToDto(savedExchange);
    }

    public ExchangeHistoryDto getExchangeHistoryById(Long id) {
        ExchangeHistory exchange = exchangeHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 내역을 찾을 수 없습니다: " + id));
        return convertToDto(exchange);
    }

    public List<ExchangeHistoryDto> getAllExchangeHistory() {
        return exchangeHistoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ExchangeHistoryDto> getExchangeHistoryByUserId(Long userId) {
        return exchangeHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ExchangeHistoryDto> getExchangeHistoryByProductName(String productName) {
        return exchangeHistoryRepository.findByProductNameOrderByCreatedAtDesc(productName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ExchangeHistoryDto> getExchangeHistoryByUserIdAndProductName(Long userId, String productName) {
        return exchangeHistoryRepository.findByUserIdAndProductNameOrderByCreatedAtDesc(userId, productName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ExchangeHistoryDto updateExchangeHistory(Long id, String productName, Integer quantity, BigDecimal totalAmount) {
        ExchangeHistory exchange = exchangeHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 내역을 찾을 수 없습니다: " + id));

        exchange.setProductName(productName);
        exchange.setQuantity(quantity);
        exchange.setTotalAmount(totalAmount);

        ExchangeHistory updatedExchange = exchangeHistoryRepository.save(exchange);
        return convertToDto(updatedExchange);
    }

    public void deleteExchangeHistory(Long id) {
        if (!exchangeHistoryRepository.existsById(id)) {
            throw new RuntimeException("교환 내역을 찾을 수 없습니다: " + id);
        }
        exchangeHistoryRepository.deleteById(id);
    }

    private ExchangeHistoryDto convertToDto(ExchangeHistory exchange) {
        ExchangeHistoryDto dto = new ExchangeHistoryDto();
        dto.setId(exchange.getId());
        dto.setUserId(exchange.getUserId());
        dto.setProductName(exchange.getProductName());
        dto.setQuantity(exchange.getQuantity());
        dto.setTotalAmount(exchange.getTotalAmount());
        dto.setCreatedAt(exchange.getCreatedAt());
        dto.setUpdatedAt(exchange.getUpdatedAt());
        return dto;
    }
}
