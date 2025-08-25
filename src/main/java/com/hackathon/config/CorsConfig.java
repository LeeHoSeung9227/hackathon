package com.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS 설정이 SecurityConfig에서 중앙 관리되므로 이 클래스는 비활성화
// @Configuration
public class CorsConfig implements WebMvcConfigurer {

    // @Override
    public void addCorsMappings(CorsRegistry registry) {
        // SecurityConfig에서 CORS 설정을 관리하므로 여기서는 설정하지 않음
        // registry.addMapping("/**")
        //         .allowedOrigins("http://localhost:3000", "http://localhost:3001")
        //         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        //         .allowedHeaders("*")
        //         .allowCredentials(true)
        //         .maxAge(3600);
    }

    // @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // SecurityConfig에서 CORS 설정을 관리하므로 여기서는 설정하지 않음
        // CorsConfiguration configuration = new CorsConfiguration();
        // configuration.addAllowedOrigin("http://localhost:3000");
        // configuration.addAllowedOrigin("http://localhost:3001");
        // configuration.addAllowedMethod("*");
        // configuration.addAllowedHeader("*");
        // configuration.setAllowCredentials(true);
        
        // UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", configuration);
        // return source;
        return null;
    }
}

