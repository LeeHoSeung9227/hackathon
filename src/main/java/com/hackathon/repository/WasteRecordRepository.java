package com.hackathon.repository;

import com.hackathon.entity.WasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {
    List<WasteRecord> findByUserIdOrderByRecordedAtDesc(Long userId);
    List<WasteRecord> findByUserIdAndStatus(Long userId, String status);
}

