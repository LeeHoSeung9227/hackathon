package com.hackathon.service.a;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.dto.a.ImageAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ChatGptImageAnalysisService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ImageCompressionService imageCompressionService;
    
    private final String apiKey;
    
    public ChatGptImageAnalysisService(@Value("${openai.api.key}") String apiKey, 
                                     ImageCompressionService imageCompressionService) {
        this.apiKey = apiKey;
        this.imageCompressionService = imageCompressionService;
        log.info("ChatGptImageAnalysisService 생성자 실행 - API 키: {}...", apiKey.substring(0, Math.min(20, apiKey.length())));
    }
    
    // 포인트 시스템 정의
    private static final Map<String, Integer> POINT_MAP = Map.of(
        "PET-순수함", 10,
        "CAN-순수함", 8,
        "PAPER-순수함", 5,
        "GLASS-순수함", 7,
        "PLASTIC-순수함", 6
    );
    
    public ImageAnalysisResult analyzeImage(byte[] imageBytes) {
        try {
            log.info("이미지 분석 시작: {} bytes", imageBytes.length);
            
            // 자동 이미지 압축 (50KB 이하로)
            byte[] compressedImage = imageCompressionService.compressImage(imageBytes, 50);
            log.info("이미지 압축 완료: {} bytes → {} bytes", imageBytes.length, compressedImage.length);
            
            // 이미지를 base64로 인코딩
            String base64Image = Base64.getEncoder().encodeToString(compressedImage);
            log.info("Base64 인코딩 완료: {} characters", base64Image.length());
            
            // ChatGPT API 요청 생성
            log.info("ChatGPT API 호출 시작");
            String analysisResult = callChatGptApi(base64Image);
            log.info("ChatGPT API 응답: {}", analysisResult);
            
            // 결과 파싱
            log.info("응답 파싱 시작");
            ImageAnalysisResult result = parseAnalysisResult(analysisResult);
            log.info("파싱 완료: {}", result);
            
            return result;
            
        } catch (Exception e) {
            log.error("이미지 분석 중 오류 발생", e);
            return ImageAnalysisResult.builder()
                .classification("분석실패")
                .reason("API 호출 중 오류 발생: " + e.getMessage())
                .pointsEarned(0)
                .wasteType("UNKNOWN")
                .isRecyclable(false)
                .build();
        }
    }
    
    private String callChatGptApi(String base64Image) {
        try {
            log.info("ChatGPT API 호출 시작");
            String url = "https://api.openai.com/v1/chat/completions";
            
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            log.info("API 키 설정 완료: {}...", apiKey.substring(0, Math.min(20, apiKey.length())));
            
            // 한국어로 응답하도록 프롬프트 설정
            String systemPrompt = "이미지를 보고 쓰레기 분류를 한국어로 답변하세요. 재질은 다음 중 하나로 정확히 분류하세요: PET, CAN, PAPER, GLASS, PLASTIC, PP, PS, PVC, HDPE, LDPE, ALUMINUM, STEEL. 상태는 다음 중 하나로 판별하세요: 순수함(라벨X, 음식물,음료료 X,깨끗함), 보통(약간의 흔적), 오염(음식물이나 색이 다른 액체), 라벨(라벨이 있음). 형식: '재질-상태,이유' (예: GLASS-순수함,투명컵으로 라벨제거됨)";
            String userPrompt = "이 이미지의 쓰레기 분류를 위의 재질 중 하나로 정확히 분류하고, 상태를 신중하게 판별해주세요. 라벨이 있다면 '라벨'으로, 실제로 음료나 음식물이 남아있다면 '오염'으로, 깨끗하다면 '순수함'으로 어느정도 괜찮다면 '보통'으로 분류해주세요.";
            log.info("프롬프트 설정: system='{}', user='{}'", systemPrompt, userPrompt);
            
            // 요청 본문 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o-mini"); // 이미지 지원 모델로 변경
            requestBody.put("max_tokens", 200); // 한국어 응답을 위해 토큰 수 증가
            
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", userPrompt);
            
            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            Map<String, String> imageUrl = new HashMap<>();
            imageUrl.put("url", "data:image/jpeg;base64," + base64Image);
            imageContent.put("image_url", imageUrl);
            
            userMessage.put("content", java.util.Arrays.asList(
                Map.of("type", "text", "text", userPrompt),
                imageContent
            ));
            
            requestBody.put("messages", java.util.Arrays.asList(systemMessage, userMessage));
            log.info("요청 본문 생성 완료: model={}, max_tokens={}", "gpt-4o-mini", 100);
            
            // API 호출
            log.info("ChatGPT API 호출 중...");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            
            log.info("API 응답 수신: 상태코드={}", response.getStatusCode());
            log.info("응답 본문: {}", response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    JsonNode responseJson = objectMapper.readTree(response.getBody());
                    String content = responseJson.get("choices").get(0).get("message").get("content").asText();
                    log.info("응답 내용 추출: {}", content);
                    return content;
                } catch (Exception e) {
                    log.error("ChatGPT API 응답 파싱 실패", e);
                    throw new RuntimeException("API 응답 파싱 실패: " + e.getMessage());
                }
            } else {
                log.error("ChatGPT API 호출 실패 - 상태코드: {}, 응답: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("ChatGPT API 호출 실패: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("ChatGPT API 호출 중 예외 발생", e);
            throw e;
        }
    }
    
    private boolean isRecyclableWaste(String wasteType) {
        if (wasteType == null || wasteType.trim().isEmpty()) {
            return false;
        }
        
        String type = wasteType.trim().toUpperCase();
        return type.equals("PET") || type.equals("CAN") || type.equals("PAPER") || 
               type.equals("GLASS") || type.equals("PLASTIC") || type.equals("PP") ||
               type.equals("PS") || type.equals("PVC") || type.equals("HDPE") ||
               type.equals("LDPE") || type.equals("ALUMINUM") || type.equals("STEEL");
    }
    
    private int calculatePoints(String wasteType, String status) {
        // 재활용 불가능한 재질이면 0점
        if (!isRecyclableWaste(wasteType)) {
            return 0;
        }
        
        // 상태가 "순수함"이 아니면 0점
        if (status.contains("순수함") || status.contains("보통")) {
            return 10;
        }
        
        // 재활용 가능 + 순수한 상태 = 10점
        return 10;
    }
    
    private ImageAnalysisResult parseAnalysisResult(String analysisResult) {
        try {
            log.info("분석 결과 파싱 시작: {}", analysisResult);
            
            // 응답이 null이거나 비어있는 경우 처리
            if (analysisResult == null || analysisResult.trim().isEmpty()) {
                log.warn("ChatGPT API 응답이 비어있음");
                return ImageAnalysisResult.builder()
                    .classification("응답없음")
                    .reason("API 응답이 비어있음")
                    .pointsEarned(0)
                    .wasteType("UNKNOWN")
                    .isRecyclable(false)
                    .build();
            }
            
            // "PET-순수함,라벨제거됨" 형태 파싱
            String[] parts = analysisResult.split(",");
            String classification = "";
            String reason = "";
            
            if (parts.length >= 2) {
                classification = parts[0].trim();
                reason = parts[1].trim();
            }
            
            log.info("파싱 결과: 분류='{}', 이유='{}'", classification, reason);
            
            // 분류가 비어있는 경우 기본값 설정
            if (classification.isEmpty()) {
                classification = "일반쓰레기";
                reason = "분류 불가";
            }
            
            // 분류에서 재질과 상태 추출 (예: PET-순수함)
            String[] classificationParts = classification.split("-");
            String wasteType = "일반쓰레기";
            String status = "";
            
            if (classificationParts.length >= 2) {
                wasteType = classificationParts[0].trim();
                status = classificationParts[1].trim();
            } else if (classificationParts.length == 1) {
                wasteType = classificationParts[0].trim();
            }
            
            // 재활용 가능 여부 판단 (재질별로 판단)
            boolean isRecyclable = isRecyclableWaste(wasteType) && 
                      !status.contains("오염") && 
                      !status.contains("손상") && 
                      !status.contains("라벨");
            
            // 포인트 계산: 순수한 상태만 10점
            int points = calculatePoints(wasteType, status);
            
            log.info("최종 결과: 재질='{}', 상태='{}', 포인트={}, 재활용가능={}", 
                wasteType, status, points, isRecyclable);
            
            return ImageAnalysisResult.builder()
                .classification(classification)
                .reason(reason)
                .pointsEarned(points)
                .wasteType(wasteType)
                .isRecyclable(isRecyclable)
                .build();
                
        } catch (Exception e) {
            log.error("분석 결과 파싱 실패: {}", analysisResult, e);
            return ImageAnalysisResult.builder()
                .classification("파싱실패")
                .reason("결과 형식 오류: " + e.getMessage())
                .pointsEarned(0)
                .wasteType("UNKNOWN")
                .isRecyclable(false)
                .build();
        }
    }
}
