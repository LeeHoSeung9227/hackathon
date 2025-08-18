package com.hackathon.repository.common;

import com.hackathon.entity.common.WasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {
    List<WasteRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<WasteRecord> findByWasteTypeOrderByCreatedAtDesc(String wasteType);
    List<WasteRecord> findByUserIdAndWasteTypeOrderByCreatedAtDesc(Long userId, String wasteType);
}
