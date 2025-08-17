package com.hackathon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TacoModelService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * TACO 모델을 실행하여 쓰레기 탐지 결과를 반환
     */
    public Map<String, Object> detectWaste(MultipartFile imageFile) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 이미지 파일을 임시로 저장
            String tempImagePath = saveTempImage(imageFile);
            System.out.println("임시 이미지 저장 경로: " + tempImagePath);
            
            // 2. Python TACO 모델 실행
            System.out.println("Python TACO 모델 실행 시작...");
            Map<String, Object> modelResult = runTacoModel(tempImagePath);
            System.out.println("Python 모델 실행 결과: " + modelResult);
            
            // 3. 결과 처리
            if (modelResult.containsKey("success") && (Boolean) modelResult.get("success")) {
                result.put("success", true);
                result.put("detections", modelResult.get("detections"));
                result.put("wasteTypes", modelResult.get("class_ids"));
                result.put("confidence", modelResult.get("scores"));
                result.put("message", "쓰레기 탐지 성공!");
            } else {
                result.put("success", false);
                result.put("message", "쓰레기를 탐지할 수 없습니다.");
            }
            
            // 4. 임시 파일 삭제
            cleanupTempFile(tempImagePath);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "모델 실행 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }

    public Map<String, Object> getModelStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "ready");
        status.put("message", "TACO 모델이 준비되었습니다.");
        status.put("model", "mask_rcnn_taco_0007.h5");
        status.put("timestamp", LocalDateTime.now().toString());
        return status;
    }
    
    /**
     * 이미지 파일을 임시로 저장
     */
    private String saveTempImage(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "taco_images");
        
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
        
        Path tempImagePath = tempDir.resolve(fileName);
        Files.copy(imageFile.getInputStream(), tempImagePath);
        
        return tempImagePath.toString();
    }
    
    /**
     * Python TACO 모델 실행
     */
    private Map<String, Object> runTacoModel(String imagePath) throws IOException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Python 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder(
                "python", 
                "C:\\Users\\COEL_03\\Desktop\\hackathon\\taco_detector.py",  // TACO 모델 실행 스크립트
                imagePath
            );
            
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // 결과 읽기 (Java 8 호환)
            StringBuilder output = new StringBuilder();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            String outputStr = output.toString();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // JSON 결과 파싱
                JsonNode jsonResult = objectMapper.readTree(outputStr);
                
                // 안전한 JSON 파싱
                boolean success = jsonResult.has("success") ? jsonResult.get("success").asBoolean() : false;
                int detections = jsonResult.has("detections") ? jsonResult.get("detections").asInt() : 0;
                
                JsonNode classIdsNode = jsonResult.get("class_ids");
                Object classIds = classIdsNode != null ? classIdsNode : new String[0];
                
                JsonNode scoresNode = jsonResult.get("scores");
                Object scores = scoresNode != null ? scoresNode : new double[0];
                
                result.put("success", success);
                result.put("detections", detections);
                result.put("class_ids", classIds);
                result.put("scores", scores);
            } else {
                result.put("success", false);
                result.put("message", "모델 실행 실패: " + output);
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "모델 실행 오류: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 임시 파일 정리
     */
    private void cleanupTempFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // 로그만 남기고 계속 진행
            System.err.println("임시 파일 삭제 실패: " + e.getMessage());
        }
    }
}
