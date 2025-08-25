package com.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)  // CORS 필터가 최우선 순위로 실행
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 자격증명 허용
        config.setAllowCredentials(true);
        
        // 허용할 origin 패턴들
        config.addAllowedOriginPattern("http://localhost:5173");
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://localhost:3001");
        config.addAllowedOriginPattern("https://team5-fe-seven.vercel.app");
        config.addAllowedOriginPattern("https://*.o-r.kr");
        
        // 허용할 헤더들
        config.addAllowedHeader("*");
        
        // 허용할 메서드들
        config.addAllowedMethod("*");
        
        // 노출할 헤더들
        config.addExposedHeader("Authorization");
        
        // preflight 요청 캐시 시간
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

