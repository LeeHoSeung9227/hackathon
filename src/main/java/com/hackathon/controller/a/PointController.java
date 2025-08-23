package com.hackathon.controller.a;

import com.hackathon.dto.a.PointHistoryDto;
import com.hackathon.dto.a.UserDto;
import com.hackathon.entity.a.PointHistory;
import com.hackathon.entity.a.Image;
import com.hackathon.repository.a.PointHistoryRepository;
import com.hackathon.repository.a.ImageRepository;
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
    
    // ===== ?�인??추�?/차감 =====
    
    /**
     * ?�인??추�?/차감
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPoints(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer points = Integer.valueOf(request.get("points").toString());
            String type = (String) request.get("type");
            String description = (String) request.get("description");
            
            // ?�인???�스?�리 ?�??            PointHistoryDto pointHistory = pointHistoryService.createPointHistory(userId, type, points, description);
            
            // ?�용??�??�인???�데?�트
            userService.updateUserPoints(userId, points);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", points > 0 ? "?�인?��? 추�??�었?�니??" : "?�인?��? 차감?�었?�니??",
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
     * ?�용???�인???�약 ?�보 (�??�인?? ?�재 ?�인?? ?�용 ?�인?? ?�과?� �??�인??
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserPointSummary(@PathVariable Long userId) {
        try {
            // ?�용???�보 조회
            var user = userService.getUserById(userId);
            
            // ?�인???�스?�리 조회
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // ?�인??계산
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // ?�과?� �??�인??(같�? ?�과?� ?�용?�들???�인???�계)
            int collegeTotalPoints = userService.getCollegeTotalPoints(user.getCollege());
            
            // ?�벨 ?�보
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
                    "totalEarned", totalEarned,      // �??�득 ?�인??                    "totalSpent", totalSpent,        // �??�용 ?�인??                    "currentPoints", currentPoints,  // ?�재 보유 ?�인??                    "collegeTotalPoints", collegeTotalPoints  // ?�과?� �??�인??                ),
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
    
    // ===== ?�인???�역 =====
    
    /**
     * ?�용???�인???�역 조회
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
     * ?��?지�??�인???�역 조회
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getImagePointHistory(@PathVariable Long imagesId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByImageId(imagesId);
            
            // �??�인??계산
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
     * 변�??�?�별 ?�인???�역 조회 (changeType??'all'?�면 모든 ?�??반환)
     */
    @GetMapping("/user/{userId}/type/{changeType}")
    public ResponseEntity<?> getPointHistoryByType(
            @PathVariable Long userId,
            @PathVariable String changeType) {
        
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            
            if ("all".equals(changeType)) {
                // 모든 ?�?�의 ?�인???�스?�리 조회
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
                    
                    // ?��?지 ?�보가 ?�는 경우 추�?
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
                            log.warn("?��?지 조회 ?�패: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
            } else {
                // ?�정 ?�?�의 ?�인???�스?�리 조회
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
                    
                    // ?��?지 ?�보가 ?�는 경우 추�?
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
                            log.warn("?��?지 조회 ?�패: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("?�인???�스?�리 조회 ?�패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("?�인???�스?�리 조회???�패?�습?�다: " + e.getMessage());
        }
    }

    /**
     * ?�용???�합 ?�?�보???�보 조회 (총포?�트, ?�진, ?�립?�용, ?�간 ??
     */
    @GetMapping("/user/{userId}/dashboard/comprehensive")
    public ResponseEntity<Map<String, Object>> getUserComprehensiveDashboard(@PathVariable Long userId) {
        try {
            // ?�용???�보 조회
            var user = userService.getUserById(userId);
            
            // ?�인???�스?�리 조회
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // ?�인??계산
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // ?�세 ?�인???�립 ?�보 (?��?지, 뱃�?, ?�동 ?? - 모든 ?�???�시
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
            
            // ?�계 ?�보 (?�진 분석 ?�수 ?�거)
            Map<String, Object> statistics = Map.of(
                "totalEarned", totalEarned,      // �??�득 ?�인??                "totalSpent", totalSpent,        // �??�용 ?�인??                "currentPoints", currentPoints,  // ?�재 보유 ?�인??                "badgeCount", detailedHistory.stream()
                    .filter(h -> "BADGE_EARNED".equals(h.get("type")))
                    .count(),                    // 뱃�? ?�득 ?�수
                "manualCount", detailedHistory.stream()
                    .filter(h -> "MANUAL_ADD".equals(h.get("type")))
                    .count()                     // ?�동 추�? ?�수
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
     * ?�짜 범위�??�인???�역 조회
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // ?�짜 ?�싱 (간단??구현)
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            
            // ?�인???�스?�리 조회 (기간�?
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId).stream()
                .filter(ph -> ph.getCreatedAt().isAfter(start) && ph.getCreatedAt().isBefore(end))
                .collect(Collectors.toList());
            
            // �??�인??계산
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "userId", userId,
                    "startDate", startDate,
                    "endDate", endDate,
                    "totalPoints", totalPoints,  // �??�인??추�?
                    "history", pointHistory      // ?�세 ?�역
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "?�짜 ?�식???�바르�? ?�습?�다. (YYYY-MM-DD ?�식)" + e.getMessage()));
        }
    }

    // ===== DTO 변??메서??=====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; // ?��? DTO?��?�?그�?�?반환
    }

    private String getLevelName(int level) {
        switch (level) {
            case 1:
                return "?�앗";
            case 2:
                return "?��? ?�싹";
            case 3:
                return "?�싹";
            case 4:
                return "???�싹";
            case 5:
                return "?�무";
            default:
                return "?�앗";
        }
    }
    
    private String getSourceType(String type, Long imageId) {
        if (imageId != null) {
            return "?�진";
        } else if ("BADGE_EARNED".equals(type)) {
            return "뱃�?";
        } else if ("MANUAL_ADD".equals(type)) {
            return "?�동";
        } else {
            return "기�?";
        }
    }
    
    // ===== ?�품 조회 API (?�스?�용) =====
    
    /**
     * 모든 ?�품 조회
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try {
            // ?�시�??�드코딩???�품 ?�이??반환 (?�스?�용)
            List<Map<String, Object>> products = List.of(
                Map.of(
                    "id", 1L,
                    "name", "?�코�?,
                    "description", "친환�??�활???�코�?,
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                ),
                Map.of(
                    "id", 2L,
                    "name", "?�블러",
                    "description", "?�테?�리???�블러",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                ),
                Map.of(
                    "id", 3L,
                    "name", "?�활???�트",
                    "description", "?�활??종이�?만든 ?�트",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                ),
                Map.of(
                    "id", 4L,
                    "name", "친환�???,
                    "description", "?�활???�라?�틱 ??,
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                ),
                Map.of(
                    "id", 5L,
                    "name", "?�코 ?�분",
                    "description", "?�활???�재 ?�분",
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
     * ?�품 ID�?조회
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {
            // ?�시�??�드코딩???�품 ?�이?�에??ID�?찾기 (?�스?�용)
            Map<String, Object> product = null;
            
            if (id == 1L) {
                product = Map.of(
                    "id", 1L,
                    "name", "?�코�?,
                    "description", "친환�??�활???�코�?,
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                );
            } else if (id == 2L) {
                product = Map.of(
                    "id", 2L,
                    "name", "?�블러",
                    "description", "?�테?�리???�블러",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                );
            } else if (id == 3L) {
                product = Map.of(
                    "id", 3L,
                    "name", "?�활???�트",
                    "description", "?�활??종이�?만든 ?�트",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                );
            } else if (id == 4L) {
                product = Map.of(
                    "id", 4L,
                    "name", "친환�???,
                    "description", "?�활???�라?�틱 ??,
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                );
            } else if (id == 5L) {
                product = Map.of(
                    "id", 5L,
                    "name", "?�코 ?�분",
                    "description", "?�활???�재 ?�분",
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
                    "message", "?�품??찾을 ???�습?�다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * ?�품 구매 (주문 ?�성)
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            // ?�품 ?�보 조회
            List<Map<String, Object>> products = List.of(
                Map.of("id", 1L, "name", "?�코�?, "description", "친환�??�활???�코�?, "pointsRequired", 100, "price", 15000.0, "category", "LIFESTYLE", "stockQuantity", 50, "imageUrl", "/images/products/ecobag.jpg"),
                Map.of("id", 2L, "name", "?�블러", "description", "?�테?�리???�블러", "pointsRequired", 200, "price", 25000.0, "category", "LIFESTYLE", "stockQuantity", 30, "imageUrl", "/images/products/tumbler.jpg"),
                Map.of("id", 3L, "name", "?�활???�트", "description", "?�활??종이�?만든 ?�트", "pointsRequired", 50, "price", 8000.0, "category", "STATIONERY", "stockQuantity", 100, "imageUrl", "/images/products/notebook.jpg"),
                Map.of("id", 4L, "name", "친환�???, "description", "?�활???�라?�틱 ??, "pointsRequired", 30, "price", 5000.0, "category", "STATIONERY", "stockQuantity", 200, "imageUrl", "/images/products/pen.jpg"),
                Map.of("id", 5L, "name", "?�코 ?�분", "description", "?�활???�재 ?�분", "pointsRequired", 300, "price", 35000.0, "category", "LIFESTYLE", "stockQuantity", 20, "imageUrl", "/images/products/plantpot.jpg")
            );
            
            Map<String, Object> product = products.stream()
                .filter(p -> p.get("id").equals(productId))
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "?�품??찾을 ???�습?�다."));
            }
            
            int pointsRequired = (Integer) product.get("pointsRequired");
            int totalPointsRequired = pointsRequired * quantity;
            
            // ?�용???�인???�인
            var user = userService.getUserById(userId);
            if (user.getPointsTotal() < totalPointsRequired) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "?�인?��? 부족합?�다."));
            }
            
            // ?�인??차감
            int pointsToDeduct = -totalPointsRequired;
            pointHistoryService.createPointHistory(userId, "PRODUCT_PURCHASE", pointsToDeduct, 
                product.get("name") + " 구매 (" + quantity + "�?");
            
            // ?�용??�??�인???�데?�트
            userService.updateUserPoints(userId, pointsToDeduct);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "?�품 구매가 ?�료?�었?�니??",
                "data", Map.of(
                    "orderId", System.currentTimeMillis(), // ?�시 주문 ID
                    "productName", product.get("name"),
                    "quantity", quantity,
                    "pointsSpent", totalPointsRequired,
                    "remainingPoints", user.getPointsTotal() + pointsToDeduct
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "주문 ?�성 ?�패: " + e.getMessage()));
        }
    }

    // ?��?지 조회 API
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
            log.error("?��?지 조회 ?�패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("?��?지 조회???�패?�습?�다: " + e.getMessage());
        }
    }

    // ?�제 ?��?지 ?�일 ?�공
    @GetMapping("/images/file/{imageId}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            if (image.isPresent()) {
                Image img = image.get();
                
                // ?�?�된 ?��?지 ?�이?��? ?�는지 ?�인
                if (img.getImageData() != null && img.getImageData().length > 0) {
                    log.info("?��?지 ?�일 ?�공: ID={}, ?�기={} bytes", imageId, img.getImageData().length);
                    return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(img.getContentType()))
                        .body(img.getImageData());
                } else {
                    log.warn("?��?지 ?�이?��? ?�음: ID={}", imageId);
                    return ResponseEntity.notFound().build();
                }
            } else {
                log.warn("?��?지�?찾을 ???�음: ID={}", imageId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("?��?지 ?�일 ?�공 ?�패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
