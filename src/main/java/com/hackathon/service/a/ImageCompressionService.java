package com.hackathon.service.a;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class ImageCompressionService {
    
    public byte[] compressImage(byte[] originalImage, int maxSizeKB) {
        try {
            int maxSizeBytes = maxSizeKB * 1024;
            
            if (originalImage.length <= maxSizeBytes) {
                return originalImage;
            }
            
            BufferedImage originalBufferedImage = ImageIO.read(new ByteArrayInputStream(originalImage));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(originalBufferedImage, "JPEG", outputStream);
            
            return outputStream.toByteArray();
            
        } catch (IOException e) {
            log.error("이미지 압축 중 오류 발생", e);
            return originalImage;
        }
    }
}
