package com.hackathon.controller;

import com.hackathon.dto.RankingDto;
import com.hackathon.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/{category}")
    public ResponseEntity<List<RankingDto>> getRanking(@PathVariable String category) {
        List<RankingDto> rankings = rankingService.getRankingsByCategory(category);
        return ResponseEntity.ok(rankings);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<RankingDto> getUserRank(@PathVariable Long userId) {
        RankingDto userRank = rankingService.getUserRank(userId);
        return ResponseEntity.ok(userRank);
    }
}

