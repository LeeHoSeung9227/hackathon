package com.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // HTTPS 강제는 SSL 인증서 설정 후 활성화
            // .requiresChannel(channel -> channel
            //     .anyRequest().requiresSecure())  // 모든 요청을 HTTPS로 강제
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})  // 기본 CORS 설정 사용 (disable 제거)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/ai/test/**").permitAll()  // AI 테스트 엔드포인트
                .requestMatchers("/api/ai/analyze").permitAll()  // AI 분석 엔드포인트
                .requestMatchers("/api/auth/**").permitAll()     // 인증 관련
                .requestMatchers("/api/**").permitAll()          // 기타 API
                .requestMatchers("/").permitAll()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    // CORS 설정 제거 - CorsConfig에서 관리
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() { ... }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
