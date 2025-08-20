package com.hackathon.service.common;

import com.hackathon.dto.common.WasteRecordDto;
import com.hackathon.entity.common.WasteRecord;
import com.hackathon.repository.common.WasteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WasteRecordService {
    
    private final WasteRecordRepository wasteRecordRepository;
    
    public WasteRecordDto createWasteRecord(Long userId, String wasteType, Integer points, String imageUrl) {
        WasteRecord record = new WasteRecord();
        record.setUserId(userId);
        record.setWasteType(wasteType);
        record.setPoints(points);
        record.setImageUrl(imageUrl);
        
        WasteRecord savedRecord = wasteRecordRepository.save(record);
        return convertToDto(savedRecord);
    }
    
    public WasteRecordDto getWasteRecordById(Long id) {
        WasteRecord record = wasteRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("폐기물 기록을 찾을 수 없습니다: " + id));
        return convertToDto(record);
    }
    
    public List<WasteRecordDto> getAllWasteRecords() {
        return wasteRecordRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<WasteRecordDto> getWasteRecordsByUserId(Long userId) {
        return wasteRecordRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<WasteRecordDto> getWasteRecordsByType(String wasteType) {
        return wasteRecordRepository.findByWasteTypeOrderByCreatedAtDesc(wasteType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<WasteRecordDto> getWasteRecordsByUserIdAndType(Long userId, String wasteType) {
        return wasteRecordRepository.findByUserIdAndWasteTypeOrderByCreatedAtDesc(userId, wasteType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public WasteRecordDto updateWasteRecord(Long id, String wasteType, Integer points, String imageUrl) {
        WasteRecord record = wasteRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("폐기물 기록을 찾을 수 없습니다: " + id));
        
        record.setWasteType(wasteType);
        record.setPoints(points);
        record.setImageUrl(imageUrl);
        
        WasteRecord updatedRecord = wasteRecordRepository.save(record);
        return convertToDto(updatedRecord);
    }
    
    public void deleteWasteRecord(Long id) {
        if (!wasteRecordRepository.existsById(id)) {
            throw new RuntimeException("폐기물 기록을 찾을 수 없습니다: " + id);
        }
        wasteRecordRepository.deleteById(id);
    }
    
    private WasteRecordDto convertToDto(WasteRecord record) {
        WasteRecordDto dto = new WasteRecordDto();
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setWasteType(record.getWasteType());
        dto.setPoints(record.getPoints());
        dto.setImageUrl(record.getImageUrl());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        return dto;
    }
}
