package com.hackathon.service;

import com.hackathon.dto.WasteRecordDto;
import com.hackathon.entity.WasteRecord;
import com.hackathon.entity.User;
import com.hackathon.repository.WasteRecordRepository;
import com.hackathon.repository.UserRepository;
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
    private final UserRepository userRepository;

    public List<WasteRecordDto> getRecentRecordsByUserId(Long userId, int limit) {
        return wasteRecordRepository.findByUserIdOrderByRecordedAtDesc(userId)
                .stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public WasteRecordDto createWasteRecord(Long userId, String wasteType, int earnedPoints) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        WasteRecord record = new WasteRecord();
        record.setUser(user);
        record.setWasteType(wasteType);
        record.setEarnedPoints(earnedPoints);
        record.setAccumulatedPoints(user.getPoints() + earnedPoints);
        record.setStatus("SUCCESS");

        WasteRecord savedRecord = wasteRecordRepository.save(record);
        
        // 사용자 포인트 업데이트
        user.setPoints(user.getPoints() + earnedPoints);
        userRepository.save(user);

        return convertToDto(savedRecord);
    }

    private WasteRecordDto convertToDto(WasteRecord record) {
        WasteRecordDto dto = new WasteRecordDto();
        dto.setId(record.getId());
        dto.setUserId(record.getUser().getId());
        dto.setWasteType(record.getWasteType());
        dto.setEarnedPoints(record.getEarnedPoints());
        dto.setAccumulatedPoints(record.getAccumulatedPoints());
        dto.setRecordedAt(record.getRecordedAt());
        dto.setStatus(record.getStatus());
        dto.setUserNickname(record.getUser().getNickname());
        return dto;
    }
}

