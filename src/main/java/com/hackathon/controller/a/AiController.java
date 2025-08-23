package com.hackathon.controller.a;

import com.hackathon.dto.a.AiResultDto;
import com.hackathon.dto.a.ImageDto;
import com.hackathon.dto.a.ImageAnalysisResult;
import com.hackathon.entity.a.AiResult;
import com.hackathon.entity.a.Image;
import com.hackathon.entity.a.PointHistory;
import com.hackathon.repository.a.ImageRepository;
import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.service.a.AiResultService;
import com.hackathon.service.a.ChatGptImageAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RestController
@RequestMapping("/api/ai")
public class AiController {
    
    private final AiResultService aiResultService;
    private final ImageRepository imageRepository;
    private final ChatGptImageAnalysisService chatGptImageAnalysisService;
    private final PointHistoryRepository pointHistoryRepository;
    
    public AiController(AiResultService aiResultService, ImageRepository imageRepository, ChatGptImageAnalysisService chatGptImageAnalysisService, PointHistoryRepository pointHistoryRepository) {
        this.aiResultService = aiResultService;
        this.imageRepository = imageRepository;
        this.chatGptImageAnalysisService = chatGptImageAnalysisService;
        this.pointHistoryRepository = pointHistoryRepository;
        
        // 의존성 주입 상태 확인
        log.info("=== AiController 생성자 실행 ===");
        log.info("aiResultService: {}", aiResultService != null ? "주입됨" : "주입실패");
        log.info("imageRepository: {}", imageRepository != null ? "주입됨" : "주입실패");
        log.info("chatGptImageAnalysisService: {}", chatGptImageAnalysisService != null ? "주입됨" : "주입실패");
        log.info("=== AiController 생성자 완료 ===");
    }
    
    /**
     * 컨트롤러 테스트용 엔드포인트
     */
    @GetMapping("/test")
    public ResponseEntity<String> testController() {
        log.info("=== GET /api/ai/test 엔드포인트 호출됨 ===");
        return ResponseEntity.ok("AiController 정상 작동 중!");
    }
    
    /**
     * POST 요청 테스트용 엔드포인트
     */
    @PostMapping("/test-post")
    public ResponseEntity<String> testPostController() {
        log.info("=== POST /api/ai/test-post 엔드포인트 호출됨 ===");
        return ResponseEntity.ok("AiController POST 요청 정상 작동 중!");
    }
    
    /**
     * 간단한 POST 테스트 엔드포인트 (파라미터 없음)
     */
    @PostMapping("/simple-test")
    public ResponseEntity<String> simplePostTest() {
        log.info("=== POST /api/ai/simple-test 엔드포인트 호출됨 ===");
        return ResponseEntity.ok("간단한 POST 요청 성공!");
    }
    
    /**
     * ChatGPT를 사용한 폐기물 이미지 분석
     */
    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisResult> analyzeWasteImageWithChatGpt(@RequestParam("image") MultipartFile imageFile) {
        log.info("=== POST /api/ai/analyze 엔드포인트 호출됨 ===");
        log.info("이미지 파일명: {}", imageFile.getOriginalFilename());
        log.info("이미지 크기: {} bytes", imageFile.getSize());
        log.info("이미지 타입: {}", imageFile.getContentType());
        
        try {
            if (imageFile.isEmpty()) {
                log.warn("❌ 이미지 파일이 비어있음");
                return ResponseEntity.badRequest()
                    .body(ImageAnalysisResult.builder()
                        .classification("파일오류")
                        .reason("이미지 파일이 비어있습니다")
                        .pointsEarned(0)
                        .wasteType("UNKNOWN")
                        .isRecyclable(false)
                        .build());
            }
            
            log.info("✅ 이미지 파일 검증 완료");
            log.info("ChatGptImageAnalysisService 주입 확인: {}", chatGptImageAnalysisService != null ? "성공" : "실패");
            
            byte[] imageBytes = imageFile.getBytes();
            log.info("✅ 이미지 바이트 배열 변환 완료: {} bytes", imageBytes.length);
            
            log.info("🚀 ChatGPT API 호출 시작");
            ImageAnalysisResult result = chatGptImageAnalysisService.analyzeImage(imageBytes);
            log.info("✅ ChatGPT API 호출 완료: {}", result);
            
            // 포인트 히스토리에 저장 (10점을 받았을 때만)
            if (result.getPointsEarned() == 10) {
                try {
                    PointHistory pointHistory = new PointHistory();
                    pointHistory.setUserId(1L); // 임시 사용자 ID (나중에 실제 로그인된 사용자 ID로 변경)
                    pointHistory.setType("AI_ANALYSIS");
                    pointHistory.setPoints(10); // 항상 10점
                    pointHistory.setDescription(
                        String.format("%s 분리수거로 10점 획득 (%s)", 
                            result.getWasteType(), 
                            result.getReason())
                    );
                    pointHistory.setCreatedAt(LocalDateTime.now());
                    pointHistory.setUpdatedAt(LocalDateTime.now());
                    
                    pointHistoryRepository.save(pointHistory);
                    log.info("✅ 포인트 히스토리 저장 완료: 분리수거로 10점 획득");
                } catch (Exception e) {
                    log.warn("⚠️ 포인트 히스토리 저장 실패: {}", e.getMessage());
                }
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("❌ ChatGPT 이미지 분석 중 오류 발생", e);
            log.error("오류 타입: {}", e.getClass().getSimpleName());
            log.error("오류 메시지: {}", e.getMessage());
            
            return ResponseEntity.internalServerError()
                .body(ImageAnalysisResult.builder()
                    .classification("분석실패")
                    .reason("서버 오류: " + e.getMessage())
                    .pointsEarned(0)
                    .wasteType("UNKNOWN")
                    .isRecyclable(false)
                    .build());
        }
    }
    
    // ===== AI 분석 결과 =====
    
    /**
     * 이미지별 AI 분석 결과 조회
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getAiResultByImage(@PathVariable Long imagesId) {
        try {
            List<AiResultDto> aiResultDtos = aiResultService.getAiResultsByImageId(imagesId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            log.error("이미지별 AI 분석 결과 조회 중 오류 발생", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 사용자별 AI 분석 결과 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserAiResults(@PathVariable Long userId) {
        try {
            List<AiResultDto> aiResultDtos = aiResultService.getAiResultsByUserId(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            log.error("사용자별 AI 분석 결과 조회 중 오류 발생", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 재질 타입별 AI 분석 결과 조회
     */
    @GetMapping("/material/{materialType}")
    public ResponseEntity<Map<String, Object>> getAiResultsByMaterialType(@PathVariable String materialType) {
        try {
            List<AiResultDto> aiResultDtos = aiResultService.getAiResultsByWasteType(materialType);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            log.error("재질 타입별 AI 분석 결과 조회 중 오류 발생", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 승인 상태별 AI 분석 결과 조회
     */
    @GetMapping("/status/{isApproved}")
    public ResponseEntity<Map<String, Object>> getAiResultsByApprovalStatus(@PathVariable Boolean isApproved) {
        try {
            return ResponseEntity.badRequest().body(Map.of("error", "승인 상태별 조회는 지원하지 않습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 AI 분석 결과 조회
     */
    @GetMapping("/range")
    public ResponseEntity<Map<String, Object>> getAiResultsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<AiResultDto> aiResultDtos = aiResultService.getAiResultsByDateRange(start, end);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", aiResultDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== 이미지 관리 =====
    
    /**
     * 사용자 이미지 목록 조회
     */
    @GetMapping("/images/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserImages(@PathVariable Long userId) {
        try {
            List<Image> images = imageRepository.findByUserIdOrderByCreatedAtDesc(userId);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 상태별 이미지 조회
     */
    @GetMapping("/images/status/{status}")
    public ResponseEntity<Map<String, Object>> getImagesByStatus(@PathVariable String status) {
        return ResponseEntity.badRequest().body(Map.of("error", "이미지 상태별 조회는 지원하지 않습니다."));
    }
    
    /**
     * 사용자별 상태별 이미지 조회
     */
    @GetMapping("/images/user/{userId}/status/{status}")
    public ResponseEntity<Map<String, Object>> getUserImagesByStatus(
            @PathVariable Long userId, 
            @PathVariable String status) {
        return ResponseEntity.badRequest().body(Map.of("error", "사용자/상태별 조회는 지원하지 않습니다."));
    }
    
    /**
     * 날짜 범위별 이미지 조회
     */
    @GetMapping("/images/range")
    public ResponseEntity<Map<String, Object>> getImagesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<Image> images = imageRepository.findByCreatedAtBetween(start, end);
            
            List<ImageDto> imageDtos = images.stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", imageDtos
            ));
        } catch (Exception e) {
            log.error("날짜 범위별 이미지 조회 중 오류 발생", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 이미지 상세 정보 조회
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<Map<String, Object>> getImageById(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            
            if (image.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", convertToImageDto(image.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "이미지를 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            log.error("이미지 상세 정보 조회 중 오류 발생", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private AiResultDto convertToDto(AiResult aiResult) {
        return AiResultDto.builder()
                .id(aiResult.getId())
                .imageId(aiResult.getImageId())
                .userId(aiResult.getUserId())
                .wasteType(aiResult.getWasteType())
                .confidence(aiResult.getConfidence())
                .resultData(aiResult.getResultData())
                .createdAt(aiResult.getCreatedAt())
                .updatedAt(aiResult.getUpdatedAt())
                .build();
    }
    
    private ImageDto convertToImageDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .userId(image.getUserId())
                .imageUrl(image.getImageUrl())
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }
}
