package com.hackathon.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TacoModelService {
    
    public CompletableFuture<String> analyzeWasteImage(String imagePath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("python", "taco_model.py", imagePath);
                Process process = processBuilder.start();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    return output.toString().trim();
                } else {
                    throw new RuntimeException("TACO 모델 실행 실패: " + exitCode);
                }
                
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("TACO 모델 실행 중 오류 발생", e);
            }
        });
    }
}
