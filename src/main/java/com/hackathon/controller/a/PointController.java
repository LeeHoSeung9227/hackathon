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
    
    // ===== ?¬ì¸??ì¶”ê?/ì°¨ê° =====
    
    /**
     * ?¬ì¸??ì¶”ê?/ì°¨ê°
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPoints(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer points = Integer.valueOf(request.get("points").toString());
            String type = (String) request.get("type");
            String description = (String) request.get("description");
            
            // ?¬ì¸???ˆìŠ¤? ë¦¬ ?€??            PointHistoryDto pointHistory = pointHistoryService.createPointHistory(userId, type, points, description);
            
            // ?¬ìš©??ì´??¬ì¸???…ë°?´íŠ¸
            userService.updateUserPoints(userId, points);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", points > 0 ? "?¬ì¸?¸ê? ì¶”ê??˜ì—ˆ?µë‹ˆ??" : "?¬ì¸?¸ê? ì°¨ê°?˜ì—ˆ?µë‹ˆ??",
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
     * ?¬ìš©???¬ì¸???”ì•½ ?•ë³´ (ì´??¬ì¸?? ?„ì¬ ?¬ì¸?? ?¬ìš© ?¬ì¸?? ?¨ê³¼?€ ì´??¬ì¸??
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserPointSummary(@PathVariable Long userId) {
        try {
            // ?¬ìš©???•ë³´ ì¡°íšŒ
            var user = userService.getUserById(userId);
            
            // ?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // ?¬ì¸??ê³„ì‚°
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // ?¨ê³¼?€ ì´??¬ì¸??(ê°™ì? ?¨ê³¼?€ ?¬ìš©?ë“¤???¬ì¸???©ê³„)
            int collegeTotalPoints = userService.getCollegeTotalPoints(user.getCollege());
            
            // ?ˆë²¨ ?•ë³´
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
                    "totalEarned", totalEarned,      // ì´??ë“ ?¬ì¸??                    "totalSpent", totalSpent,        // ì´??¬ìš© ?¬ì¸??                    "currentPoints", currentPoints,  // ?„ì¬ ë³´ìœ  ?¬ì¸??                    "collegeTotalPoints", collegeTotalPoints  // ?¨ê³¼?€ ì´??¬ì¸??                ),
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
    
    // ===== ?¬ì¸???´ì—­ =====
    
    /**
     * ?¬ìš©???¬ì¸???´ì—­ ì¡°íšŒ
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
     * ?´ë?ì§€ë³??¬ì¸???´ì—­ ì¡°íšŒ
     */
    @GetMapping("/image/{imagesId}")
    public ResponseEntity<Map<String, Object>> getImagePointHistory(@PathVariable Long imagesId) {
        try {
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByImageId(imagesId);
            
            // ì´??¬ì¸??ê³„ì‚°
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
     * ë³€ê²??€?…ë³„ ?¬ì¸???´ì—­ ì¡°íšŒ (changeType??'all'?´ë©´ ëª¨ë“  ?€??ë°˜í™˜)
     */
    @GetMapping("/user/{userId}/type/{changeType}")
    public ResponseEntity<?> getPointHistoryByType(
            @PathVariable Long userId,
            @PathVariable String changeType) {
        
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            
            if ("all".equals(changeType)) {
                // ëª¨ë“  ?€?…ì˜ ?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ
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
                    
                    // ?´ë?ì§€ ?•ë³´ê°€ ?ˆëŠ” ê²½ìš° ì¶”ê?
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
                            log.warn("?´ë?ì§€ ì¡°íšŒ ?¤íŒ¨: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
            } else {
                // ?¹ì • ?€?…ì˜ ?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ
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
                    
                    // ?´ë?ì§€ ?•ë³´ê°€ ?ˆëŠ” ê²½ìš° ì¶”ê?
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
                            log.warn("?´ë?ì§€ ì¡°íšŒ ?¤íŒ¨: {}", e.getMessage());
                        }
                    }
                    
                    result.add(historyMap);
                }
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ ?¤íŒ¨: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ???¤íŒ¨?ˆìŠµ?ˆë‹¤: " + e.getMessage());
        }
    }

    /**
     * ?¬ìš©???µí•© ?€?œë³´???•ë³´ ì¡°íšŒ (ì´í¬?¸íŠ¸, ?¬ì§„, ?ë¦½?´ìš©, ?œê°„ ??
     */
    @GetMapping("/user/{userId}/dashboard/comprehensive")
    public ResponseEntity<Map<String, Object>> getUserComprehensiveDashboard(@PathVariable Long userId) {
        try {
            // ?¬ìš©???•ë³´ ì¡°íšŒ
            var user = userService.getUserById(userId);
            
            // ?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId);
            
            // ?¬ì¸??ê³„ì‚°
            int totalEarned = pointHistory.stream()
                .filter(ph -> ph.getPoints() > 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            int totalSpent = Math.abs(pointHistory.stream()
                .filter(ph -> ph.getPoints() < 0)
                .mapToInt(PointHistoryDto::getPoints)
                .sum());
            
            int currentPoints = user.getPointsTotal();
            
            // ?ì„¸ ?¬ì¸???ë¦½ ?•ë³´ (?´ë?ì§€, ë±ƒì?, ?˜ë™ ?? - ëª¨ë“  ?€???œì‹œ
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
            
            // ?µê³„ ?•ë³´ (?¬ì§„ ë¶„ì„ ?Ÿìˆ˜ ?œê±°)
            Map<String, Object> statistics = Map.of(
                "totalEarned", totalEarned,      // ì´??ë“ ?¬ì¸??                "totalSpent", totalSpent,        // ì´??¬ìš© ?¬ì¸??                "currentPoints", currentPoints,  // ?„ì¬ ë³´ìœ  ?¬ì¸??                "badgeCount", detailedHistory.stream()
                    .filter(h -> "BADGE_EARNED".equals(h.get("type")))
                    .count(),                    // ë±ƒì? ?ë“ ?Ÿìˆ˜
                "manualCount", detailedHistory.stream()
                    .filter(h -> "MANUAL_ADD".equals(h.get("type")))
                    .count()                     // ?˜ë™ ì¶”ê? ?Ÿìˆ˜
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
     * ? ì§œ ë²”ìœ„ë³??¬ì¸???´ì—­ ì¡°íšŒ
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<Map<String, Object>> getUserPointHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // ? ì§œ ?Œì‹± (ê°„ë‹¨??êµ¬í˜„)
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            
            // ?¬ì¸???ˆìŠ¤? ë¦¬ ì¡°íšŒ (ê¸°ê°„ë³?
            List<PointHistoryDto> pointHistory = pointHistoryService.getPointHistoryByUserId(userId).stream()
                .filter(ph -> ph.getCreatedAt().isAfter(start) && ph.getCreatedAt().isBefore(end))
                .collect(Collectors.toList());
            
            // ì´??¬ì¸??ê³„ì‚°
            int totalPoints = pointHistory.stream()
                .mapToInt(PointHistoryDto::getPoints)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "userId", userId,
                    "startDate", startDate,
                    "endDate", endDate,
                    "totalPoints", totalPoints,  // ì´??¬ì¸??ì¶”ê?
                    "history", pointHistory      // ?ì„¸ ?´ì—­
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "? ì§œ ?•ì‹???¬ë°”ë¥´ì? ?ŠìŠµ?ˆë‹¤. (YYYY-MM-DD ?•ì‹)" + e.getMessage()));
        }
    }

    // ===== DTO ë³€??ë©”ì„œ??=====
    
    private PointHistoryDto convertToDto(PointHistoryDto dto) {
        return dto; // ?´ë? DTO?´ë?ë¡?ê·¸ë?ë¡?ë°˜í™˜
    }

    private String getLevelName(int level) {
        switch (level) {
            case 1:
                return "?¨ì•—";
            case 2:
                return "?‘ì? ?ˆì‹¹";
            case 3:
                return "?ˆì‹¹";
            case 4:
                return "???ˆì‹¹";
            case 5:
                return "?˜ë¬´";
            default:
                return "?¨ì•—";
        }
    }
    
    private String getSourceType(String type, Long imageId) {
        if (imageId != null) {
            return "?¬ì§„";
        } else if ("BADGE_EARNED".equals(type)) {
            return "ë±ƒì?";
        } else if ("MANUAL_ADD".equals(type)) {
            return "?˜ë™";
        } else {
            return "ê¸°í?";
        }
    }
    
    // ===== ?í’ˆ ì¡°íšŒ API (?ŒìŠ¤?¸ìš©) =====
    
    /**
     * ëª¨ë“  ?í’ˆ ì¡°íšŒ
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try {
            // ?„ì‹œë¡??˜ë“œì½”ë”©???í’ˆ ?°ì´??ë°˜í™˜ (?ŒìŠ¤?¸ìš©)
            List<Map<String, Object>> products = List.of(
                Map.of(
                    "id", 1L,
                    "name", "?ì½”ë°?,
                    "description", "ì¹œí™˜ê²??¬í™œ???ì½”ë°?,
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                ),
                Map.of(
                    "id", 2L,
                    "name", "?€ë¸”ëŸ¬",
                    "description", "?¤í…Œ?¸ë¦¬???€ë¸”ëŸ¬",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                ),
                Map.of(
                    "id", 3L,
                    "name", "?¬í™œ???¸íŠ¸",
                    "description", "?¬í™œ??ì¢…ì´ë¡?ë§Œë“  ?¸íŠ¸",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                ),
                Map.of(
                    "id", 4L,
                    "name", "ì¹œí™˜ê²???,
                    "description", "?¬í™œ???Œë¼?¤í‹± ??,
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                ),
                Map.of(
                    "id", 5L,
                    "name", "?ì½” ?”ë¶„",
                    "description", "?¬í™œ???Œì¬ ?”ë¶„",
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
     * ?í’ˆ IDë¡?ì¡°íšŒ
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {
            // ?„ì‹œë¡??˜ë“œì½”ë”©???í’ˆ ?°ì´?°ì—??IDë¡?ì°¾ê¸° (?ŒìŠ¤?¸ìš©)
            Map<String, Object> product = null;
            
            if (id == 1L) {
                product = Map.of(
                    "id", 1L,
                    "name", "?ì½”ë°?,
                    "description", "ì¹œí™˜ê²??¬í™œ???ì½”ë°?,
                    "price", 15000.00,
                    "pointsRequired", 100,
                    "stockQuantity", 50,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/ecobag.jpg"
                );
            } else if (id == 2L) {
                product = Map.of(
                    "id", 2L,
                    "name", "?€ë¸”ëŸ¬",
                    "description", "?¤í…Œ?¸ë¦¬???€ë¸”ëŸ¬",
                    "price", 25000.00,
                    "pointsRequired", 200,
                    "stockQuantity", 30,
                    "category", "LIFESTYLE",
                    "imageUrl", "/images/products/tumbler.jpg"
                );
            } else if (id == 3L) {
                product = Map.of(
                    "id", 3L,
                    "name", "?¬í™œ???¸íŠ¸",
                    "description", "?¬í™œ??ì¢…ì´ë¡?ë§Œë“  ?¸íŠ¸",
                    "price", 8000.00,
                    "pointsRequired", 50,
                    "stockQuantity", 100,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/notebook.jpg"
                );
            } else if (id == 4L) {
                product = Map.of(
                    "id", 4L,
                    "name", "ì¹œí™˜ê²???,
                    "description", "?¬í™œ???Œë¼?¤í‹± ??,
                    "price", 5000.00,
                    "pointsRequired", 30,
                    "stockQuantity", 200,
                    "category", "STATIONERY",
                    "imageUrl", "/images/products/pen.jpg"
                );
            } else if (id == 5L) {
                product = Map.of(
                    "id", 5L,
                    "name", "?ì½” ?”ë¶„",
                    "description", "?¬í™œ???Œì¬ ?”ë¶„",
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
                    "message", "?í’ˆ??ì°¾ì„ ???†ìŠµ?ˆë‹¤."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * ?í’ˆ êµ¬ë§¤ (ì£¼ë¬¸ ?ì„±)
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            // ?í’ˆ ?•ë³´ ì¡°íšŒ
            List<Map<String, Object>> products = List.of(
                Map.of("id", 1L, "name", "?ì½”ë°?, "description", "ì¹œí™˜ê²??¬í™œ???ì½”ë°?, "pointsRequired", 100, "price", 15000.0, "category", "LIFESTYLE", "stockQuantity", 50, "imageUrl", "/images/products/ecobag.jpg"),
                Map.of("id", 2L, "name", "?€ë¸”ëŸ¬", "description", "?¤í…Œ?¸ë¦¬???€ë¸”ëŸ¬", "pointsRequired", 200, "price", 25000.0, "category", "LIFESTYLE", "stockQuantity", 30, "imageUrl", "/images/products/tumbler.jpg"),
                Map.of("id", 3L, "name", "?¬í™œ???¸íŠ¸", "description", "?¬í™œ??ì¢…ì´ë¡?ë§Œë“  ?¸íŠ¸", "pointsRequired", 50, "price", 8000.0, "category", "STATIONERY", "stockQuantity", 100, "imageUrl", "/images/products/notebook.jpg"),
                Map.of("id", 4L, "name", "ì¹œí™˜ê²???, "description", "?¬í™œ???Œë¼?¤í‹± ??, "pointsRequired", 30, "price", 5000.0, "category", "STATIONERY", "stockQuantity", 200, "imageUrl", "/images/products/pen.jpg"),
                Map.of("id", 5L, "name", "?ì½” ?”ë¶„", "description", "?¬í™œ???Œì¬ ?”ë¶„", "pointsRequired", 300, "price", 35000.0, "category", "LIFESTYLE", "stockQuantity", 20, "imageUrl", "/images/products/plantpot.jpg")
            );
            
            Map<String, Object> product = products.stream()
                .filter(p -> p.get("id").equals(productId))
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "?í’ˆ??ì°¾ì„ ???†ìŠµ?ˆë‹¤."));
            }
            
            int pointsRequired = (Integer) product.get("pointsRequired");
            int totalPointsRequired = pointsRequired * quantity;
            
            // ?¬ìš©???¬ì¸???•ì¸
            var user = userService.getUserById(userId);
            if (user.getPointsTotal() < totalPointsRequired) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "?¬ì¸?¸ê? ë¶€ì¡±í•©?ˆë‹¤."));
            }
            
            // ?¬ì¸??ì°¨ê°
            int pointsToDeduct = -totalPointsRequired;
            pointHistoryService.createPointHistory(userId, "PRODUCT_PURCHASE", pointsToDeduct, 
                product.get("name") + " êµ¬ë§¤ (" + quantity + "ê°?");
            
            // ?¬ìš©??ì´??¬ì¸???…ë°?´íŠ¸
            userService.updateUserPoints(userId, pointsToDeduct);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "?í’ˆ êµ¬ë§¤ê°€ ?„ë£Œ?˜ì—ˆ?µë‹ˆ??",
                "data", Map.of(
                    "orderId", System.currentTimeMillis(), // ?„ì‹œ ì£¼ë¬¸ ID
                    "productName", product.get("name"),
                    "quantity", quantity,
                    "pointsSpent", totalPointsRequired,
                    "remainingPoints", user.getPointsTotal() + pointsToDeduct
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "ì£¼ë¬¸ ?ì„± ?¤íŒ¨: " + e.getMessage()));
        }
    }

    // ?´ë?ì§€ ì¡°íšŒ API
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
            log.error("?´ë?ì§€ ì¡°íšŒ ?¤íŒ¨: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("?´ë?ì§€ ì¡°íšŒ???¤íŒ¨?ˆìŠµ?ˆë‹¤: " + e.getMessage());
        }
    }

    // ?¤ì œ ?´ë?ì§€ ?Œì¼ ?œê³µ
    @GetMapping("/images/file/{imageId}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable Long imageId) {
        try {
            var image = imageRepository.findById(imageId);
            if (image.isPresent()) {
                Image img = image.get();
                
                // ?€?¥ëœ ?´ë?ì§€ ?°ì´?°ê? ?ˆëŠ”ì§€ ?•ì¸
                if (img.getImageData() != null && img.getImageData().length > 0) {
                    log.info("?´ë?ì§€ ?Œì¼ ?œê³µ: ID={}, ?¬ê¸°={} bytes", imageId, img.getImageData().length);
                    return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(img.getContentType()))
                        .body(img.getImageData());
                } else {
                    log.warn("?´ë?ì§€ ?°ì´?°ê? ?†ìŒ: ID={}", imageId);
                    return ResponseEntity.notFound().build();
                }
            } else {
                log.warn("?´ë?ì§€ë¥?ì°¾ì„ ???†ìŒ: ID={}", imageId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("?´ë?ì§€ ?Œì¼ ?œê³µ ?¤íŒ¨: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
