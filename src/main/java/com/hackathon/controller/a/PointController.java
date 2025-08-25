package com.hackathon.controller.a;

import com.hackathon.dto.a.PointHistoryDto;
import com.hackathon.dto.a.UserDto;
import com.hackathon.entity.a.PointHistory;
import com.hackathon.entity.a.Image;
import com.hackathon.entity.a.AiResult;
import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.repository.a.ImageRepository;
import com.hackathon.repository.a.AiResultRepository;
import com.hackathon.service.a.PointHistoryService;
import com.hackathon.service.a.UserService;
import com.hackathon.service.b.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    
    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    
    private final PointHistoryService pointHistoryService;
    private final UserService userService;
    private final ProductService productService;
    private final PointHistoryRepository pointHistoryRepository;
    private final ImageRepository imageRepository;
    private final AiResultRepository aiResultRepository;
    
    // ===== Point Add/Subtract =====
    
    /**
     * Add/Subtract Points
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPoints(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer points = Integer.valueOf(request.get("points").toString());
            String type = (String) request.get("type");
            String description = (String) request.get("description");
            
            // Create point history
            PointHistoryDto pointHistory = pointHistoryService.createPointHistory(userId, type, points, description);
            
            // Update user points
            userService.updateUserPoints(userId, points);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", points > 0 ? "Points added successfully" : "Points subtracted successfully",
                "data", Map.of(
                    "pointHistory", pointHistory,
                    "pointsChanged", points,
                    "newTotalPoints", userService.getUserById(userId).getPointsTotal()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get user point summary (total earned, current points, spent points, college total points)
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserPointSummary(@PathVariable Long userId) {
        try {
            // Get user info
            var user = userService.getUserById(userId);
            
            // Get point history
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // Calculate points
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // College total points (sum of points from users in same college)
            int collegeTotalPoints = userService.getCollegeTotalPoints(user.getCollege());
            
            // Level info
            String levelName = getLevelName(user.getLevel());
            
            Map<String, Object> summary = Map.of(
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "college", user.getCollege(),
                    "campus", user.getCampus(),
                    "level", user.getLevel(),
                    "levelName", levelName
                ),
                "points", Map.of(
                    "totalEarned", totalEarned,      // Total earned points
                    "totalSpent", totalSpent,        // Total spent points
                    "currentPoints", currentPoints,  // Current points
                    "collegeTotalPoints", collegeTotalPoints  // College total points
                ),
                "recentHistory", pointHistory.stream()
                    .limit(5)
                    .collect(Collectors.toList())
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", summary
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // ===== Point History =====
    
    /**
     * Get user point history
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPointHistory(@PathVariable Long userId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            List<PointHistoryDto> pointDtos = pointHistory.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pointDtos
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get point history with images
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getImagePointHistory(@PathVariable Long imagesId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByImageId(imagesId);
            
            // 총 포인트 계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "imageId", imagesId,
                    "totalPoints", totalPoints,
                    "history", pointHistory
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get point history by change type (if changeType is 'all', return all types)
     */
    @GetMapping("/user/{userId}/type/{changeType}")
    public ResponseEntity<?> getPointHistoryByType(
            @PathVariable Long userId,
            @PathVariable String changeType) {
        
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            
            if ("all".equals(changeType)) {
                // Get all types of point history
                List<PointHistory> histories = pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
                
                for (PointHistory history : histories) {
                    Map<String, Object> historyMap = new HashMap<>();
                    historyMap.put("id", history.getId());
                    historyMap.put("userId", history.getUserId());
                    historyMap.put("type", history.getType());
                    historyMap.put("points", history.getPoints());
                    historyMap.put("description", history.getDescription());
                    historyMap.put("imageId", history.getImageId());
                    historyMap.put("createdAt", history.getCreatedAt());
                    historyMap.put("updatedAt", history.getUpdatedAt());
                    
                    // AI 분석 결과 정보 추가 (AI_ANALYSIS 타입인 경우)
                    if ("AI_ANALYSIS".equals(history.getType()) && history.getImageId() != null) {
                        try {
                            // AI 결과에서 재질 정보 가져오기
                            List<AiResult> aiResults = aiResultRepository.findByImageIdOrderByCreatedAtDesc(history.getImageId());
                            if (!aiResults.isEmpty()) {
                                AiResult aiResult = aiResults.get(0); // 가장 최근 결과
                                historyMap.put("wasteType", aiResult.getWasteType());  // ✅ AI 분석 결과 추가
                            }
                        } catch (Exception e) {
                            log.warn("AI 결과 조회 실패: {}", e.getMessage());
                        }
                    }
                    
                    // 이미지 정보가 있는 경우 추가
                    if (history.getImageId() != null) {
                        try {
                            var image = imageRepository.findById(history.getImageId());
                            if (image.isPresent()) {
                                Image img = image.get();
                                Map<String, Object> imageInfo = new HashMap<>();
                                imageInfo.put("id", img.getId());
                                imageInfo.put("filename", img.getFileName());
                                imageInfo.put("contentType", img.getContentType());
                                imageInfo.put("size", img.getFileSize());
                                imageInfo.put("url", "/api/points/images/file/" + img.getId());
                                historyMap.put("image", imageInfo);
                            }
                        } catch (Exception e) {
                            log.warn("이미지조회: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
                
                // 총 누적 포인트 계산
                int totalPoints = histories.stream()
                    .mapToInt(history -> history.getPoints())
                    .sum();
                
                Map<String, Object> response = new HashMap<>();
                response.put("totalPoint", totalPoints);      // 총 누적 포인트
                response.put("history", result);
                
                return ResponseEntity.ok(response);
                
            } else {
                // 특정 타입의 포인트 히스토리 조회
                List<PointHistory> histories = pointHistoryRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, changeType);
                
                for (PointHistory history : histories) {
                    Map<String, Object> historyMap = new HashMap<>();
                    historyMap.put("id", history.getId());
                    historyMap.put("userId", history.getUserId());
                    historyMap.put("type", history.getType());
                    historyMap.put("points", history.getPoints());
                    historyMap.put("description", history.getDescription());
                    historyMap.put("imageId", history.getImageId());
                    historyMap.put("createdAt", history.getCreatedAt());
                    historyMap.put("updatedAt", history.getUpdatedAt());
                    
                                         // 이미지 정보가 있는 경우 추가
                    if (history.getImageId() != null) {
                        try {
                            var image = imageRepository.findById(history.getImageId());
                            if (image.isPresent()) {
                                Image img = image.get();
                                Map<String, Object> imageInfo = new HashMap<>();
                                imageInfo.put("id", img.getId());
                                imageInfo.put("filename", img.getFileName());
                                imageInfo.put("contentType", img.getContentType());
                                imageInfo.put("size", img.getFileSize());
                                imageInfo.put("url", "/api/points/images/file/" + img.getId());
                                historyMap.put("image", imageInfo);
                            }
                        } catch (Exception e) {
                                                         log.warn("이미지 조회 실패: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
                         log.error("포인트 히스토리 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve point history: " + e.getMessage());
        }
    }

    /**

     */
    @GetMapping("/user/{userId}/dashboard/comprehensive")
    public ResponseEntity<Map<String, Object>> getUserComprehensiveDashboard(@PathVariable Long userId) {
        try {
            // 사용자 정보 조회
            var user = userService.getUserById(userId);
            
            // 포인트 히스토리 조회
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // 포인트 계산
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            List<Map<String, Object>> detailedHistory = pointHistory.stream()
                .map(ph -> {
                    Map<String, Object> detail = Map.of(
                        "id", ph.getId(),
                        "points", ph.getPoints(),
                        "type", ph.getType(),
                        "description", ph.getDescription(),
                        "imageId", ph.getImageId(),
                        "createdAt", ph.getCreatedAt(),
                        "source", getSourceType(ph.getType(), ph.getImageId())
                    );
                    return detail;
                })
                .collect(Collectors.toList());
            
            // Statistics info (removed photo analysis count)
            Map<String, Object> statistics = Map.of(
                "totalEarned", totalEarned,      // Total earned points
                "totalSpent", totalSpent,        // Total spent points
                "currentPoints", currentPoints,  // Current points
                "badgeCount", detailedHistory.stream()
                    .filter(h -> "BADGE_EARNED".equals(h.get("type")))
                    .count(),                    // Badge earned count
                "manualCount", detailedHistory.stream()
                    .filter(h -> "MANUAL_ADD".equals(h.get("type")))
                    .count()                     // Manual add count
            );
            
            Map<String, Object> dashboard = Map.of(
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "college", user.getCollege(),
                    "campus", user.getCampus(),
                    "level", user.getLevel(),
                    "levelName", getLevelName(user.getLevel())
                ),
                "points", statistics,
                "detailedHistory", detailedHistory,
                "recentActivity", detailedHistory.stream()
                    .limit(10)
                    .collect(Collectors.toList())
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", dashboard
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 날짜 범위별 포인트 히스토리 조회
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 날짜 파싱 (간단한 구현)
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            
            // 포인트 히스토리 조회 (기간별)
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId).stream()
                .filter(ph -> ph.getCreatedAt().isAfter(start) && ph.getCreatedAt().isBefore(end))
                .collect(Collectors.toList());
            
            // 총 포인트 계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "userId", userId,
                    "startDate", startDate,
                    "endDate", endDate,
                    "totalPoints", totalPoints, 
                    "history", pointHistory      
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식)" + e.getMessage()));
        }
    }

    // ===== DTO 변환 메서드 =====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; 
    }

    private String getLevelName(int level) {
        switch (level) {
            case 1:
                return "새싹";
            case 2:
                return "잎새";
            case 3:
                return "가지";
            case 4:
                return "나무";
            case 5:
                return "큰나무";
            default:
                return "새싹";
        }
    }
    
    private String getSourceType(String type, Long imageId) {
        if (imageId != null) {
            return "사진";
        } else if ("BADGE_EARNED".equals(type)) {
            return "뱃지";
        } else if ("MANUAL_ADD".equals(type)) {
            return "수동";
        } else {
            return "기타";
        }
    }
    

    

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try {
            // Hardcoded product list for testing
            List<Map<String, Object>> products = List.of(
                Map.of(
                    "id", 1L,
                    "name", "Eco Bag",
                    "description", "Eco-friendly reusable bag",
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                ),
                Map.of(
                    "id", 2L,
                    "name", "Tumbler",
                    "description", "Stainless steel tumbler",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                ),
                Map.of(
                    "id", 3L,
                    "name", "Eco Notebook",
                    "description", "Notebook made from recycled paper",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                ),
                Map.of(
                    "id", 4L,
                    "name", "Eco Pen",
                    "description", "Pen made from recycled plastic",
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                ),
                Map.of(
                    "id", 5L,
                    "name", "Eco Plant Pot",
                    "description", "Plant pot made from eco materials",
                    "price", 35000.00,
                    "pointsRequired", 300,
                    "stockQuantity", 20,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/plantpot.jpg"
                )
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", products
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {

            Map<String, Object> product = null;
            
            if (id == 1L) {
                product = Map.of(
                    "id", 1L,
                    "name", "Eco Bag",
                    "description", "Eco-friendly reusable bag",
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                );
            } else if (id == 2L) {
                product = Map.of(
                    "id", 2L,
                    "name", "Tumbler",
                    "description", "Stainless steel tumbler",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                );
            } else if (id == 3L) {
                product = Map.of(
                    "id", 3L,
                    "name", "Eco Notebook",
                    "description", "Notebook made from recycled paper",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                );
            } else if (id == 4L) {
                product = Map.of(
                    "id", 4L,
                    "name", "Eco Pen",
                    "description", "Pen made from recycled plastic",
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                );
            } else if (id == 5L) {
                product = Map.of(
                    "id", 5L,
                    "name", "Eco Plant Pot",
                    "description", "Plant pot made from eco materials",
                    "price", 35000.00,
                    "pointsRequired", 300,
                    "stockQuantity", 20,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/plantpot.jpg"
                );
            }
            
            if (product != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", product
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Product not found."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 상품 구매 (주문 생성)
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            // Get product info
            List<Map<String, Object>> products = List.of(
                Map.of("id", 1L, "name", "Eco Bag", "description", "Eco-friendly reusable bag", "pointsRequired", 100, "price", 15000.0, "category", "LIFESTYLE", "stockQuantity", 50, "imageUrl", "/images/products/ecobag.jpg"),
                Map.of("id", 2L, "name", "Tumbler", "description", "Stainless steel tumbler", "pointsRequired", 200, "price", 25000.0, "category", "LIFESTYLE", "stockQuantity", 30, "imageUrl", "/images/products/tumbler.jpg"),
                Map.of("id", 3L, "name", "Eco Notebook", "description", "Notebook made from recycled paper", "pointsRequired", 50, "price", 8000.0, "category", "STATIONERY", "stockQuantity", 100, "imageUrl", "/images/products/notebook.jpg"),
                Map.of("id", 4L, "name", "Eco Pen", "description", "Pen made from recycled plastic", "pointsRequired", 30, "price", 5000.0, "category", "STATIONERY", "stockQuantity", 200, "imageUrl", "/images/products/pen.jpg"),
                Map.of("id", 5L, "name", "Eco Plant Pot", "description", "Plant pot made from recycled materials", "pointsRequired", 300, "price", 35000.0, "category", "LIFESTYLE", "stockQuantity", 20, "imageUrl", "/images/products/plantpot.jpg")
            );
            
            Map<String, Object> product = products.stream()
                .filter(p -> p.get("id").equals(productId))
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Product not found."));
            }
            
            int pointsRequired = (Integer) product.get("pointsRequired");
            int totalPointsRequired = pointsRequired * quantity;
            
            // 사용자 포인트 확인
            var user = userService.getUserById(userId);
            if (user.getPointsTotal() < totalPointsRequired) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Insufficient points."));
            }
            
            // 포인트 차감
            int pointsToDeduct = -totalPointsRequired;
            pointHistoryService.createPointHistory(userId, "PRODUCT_PURCHASE", pointsToDeduct, 
                product.get("name") + " purchase (" + quantity + " qty)");
            
            // 사용자의 총 포인트 업데이트
            userService.updateUserPoints(userId, pointsToDeduct);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product purchase completed successfully.",
                "data", Map.of(
                    "orderId", System.currentTimeMillis(), // 임시 주문 ID
                    "productName", product.get("name"),
                    "quantity", quantity,
                    "pointsSpent", totalPointsRequired,
                    "remainingPoints", user.getPointsTotal() + pointsToDeduct
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Order creation failed: " + e.getMessage()));
        }
    }

    // 이미지 조회 API
    @GetMapping("/images/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            if (image.isPresent()) {
                Image img = image.get();
                Map<String, Object> imageInfo = new HashMap<>();
                imageInfo.put("id", img.getId());
                imageInfo.put("filename", img.getFileName());
                imageInfo.put("contentType", img.getContentType());
                imageInfo.put("size", img.getFileSize());
                imageInfo.put("url", "/api/points/images/" + img.getId());
                return ResponseEntity.ok(imageInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("이미지 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 조회에 실패했습니다: " + e.getMessage());
        }
    }

    // 실제 이미지 파일 제공
    @GetMapping("/images/file/{imageId}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            if (image.isPresent()) {
                Image img = image.get();
                
                // 저장된 이미지 데이터가 있는지 확인
                if (img.getImageData() != null && img.getImageData().length > 0) {
                    log.info("이미지 파일 제공: ID={}, 크기={} bytes", imageId, img.getImageData().length);
                    return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(img.getContentType()))
                        .body(img.getImageData());
                } else {
                    log.warn("이미지 데이터가 없음: ID={}", imageId);
                    return ResponseEntity.notFound().build();
                }
            } else {
                log.warn("이미지를 찾을 수 없음: ID={}", imageId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("이미지 파일 제공 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
