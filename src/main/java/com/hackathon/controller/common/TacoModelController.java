package com.hackathon.controller.common;

import com.hackathon.service.common.TacoModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/taco")
@RequiredArgsConstructor
public class TacoModelController {
    
    private final TacoModelService tacoModelService;
    private static final String UPLOAD_DIR = "uploads/";
    
    @PostMapping("/analyze")
    public CompletableFuture<ResponseEntity<String>> analyzeWasteImage(@RequestParam("image") MultipartFile file) {
        try {
            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 파일 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            // TACO 모델로 분석
            return tacoModelService.analyzeWasteImage(filePath.toString())
                    .thenApply(result -> {
                        try {
                            // 임시 파일 삭제
                            Files.deleteIfExists(filePath);
                        } catch (IOException e) {
                            // 로그 기록
                        }
                        return ResponseEntity.ok(result);
                    })
                    .exceptionally(throwable -> {
                        try {
                            // 에러 발생 시에도 임시 파일 삭제
                            Files.deleteIfExists(filePath);
                        } catch (IOException e) {
                            // 로그 기록
                        }
                        return ResponseEntity.internalServerError()
                                .body("이미지 분석 실패: " + throwable.getMessage());
                    });
                    
        } catch (IOException e) {
            return CompletableFuture.completedFuture(
                ResponseEntity.internalServerError()
                    .body("파일 업로드 실패: " + e.getMessage())
            );
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("TACO 모델 서비스 정상 동작 중");
    }
}
