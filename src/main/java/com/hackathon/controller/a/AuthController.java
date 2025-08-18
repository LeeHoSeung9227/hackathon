package com.hackathon.controller.a;

import com.hackathon.dto.a.AuthLoginDto;
import com.hackathon.dto.a.SignupRequestDto;
import com.hackathon.service.a.AuthLoginService;
import com.hackathon.service.a.SignupRequestService;
import com.hackathon.service.a.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthLoginService authLoginService;
    private final UserService userService;
    private final SignupRequestService signupRequestService;
    
    public AuthController(AuthLoginService authLoginService, UserService userService, SignupRequestService signupRequestService) {
        this.authLoginService = authLoginService;
        this.userService = userService;
        this.signupRequestService = signupRequestService;
    }
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "사용자명과 비밀번호는 필수입니다."));
            }
            
            // TODO: UserService에 authenticateUser 메서드 구현 필요
            // var user = userService.authenticateUser(username, password);
            // if (user.isEmpty()) {
            //     return ResponseEntity.badRequest()
            //         .body(Map.of("error", "잘못된 사용자명 또는 비밀번호입니다."));
            // }
            
            // 임시 로그인 로직 (실제로는 사용자 검증 필요)
            if ("admin".equals(username) && "password".equals(password)) {
                // 로그인 세션 생성 (임시 사용자 ID: 1)
                AuthLoginDto loginSession = authLoginService.createLoginSession(1L);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "로그인이 성공했습니다.",
                    "data", Map.of(
                        "userId", 1L,
                        "username", username,
                        "session", loginSession
                    )
                ));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "잘못된 사용자명 또는 비밀번호입니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            
            if (token == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "토큰은 필수입니다."));
            }
            
            // 로그인 세션 삭제
            authLoginService.deleteLoginSession(token);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그아웃이 완료되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 세션 상태 확인
     */
    @GetMapping("/session/{token}")
    public ResponseEntity<Map<String, Object>> checkSession(@PathVariable String token) {
        try {
            AuthLoginDto loginSession = authLoginService.getLoginSession(token);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", loginSession
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 회원가입 신청
     */
    @PostMapping("/signup/request")
    public ResponseEntity<Map<String, Object>> createSignupRequest(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            
            if (username == null || email == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "사용자명과 이메일은 필수입니다."));
            }
            
            // 간소화: 예시로 비밀번호/프로필 정보는 임시 값
            SignupRequestDto signupRequest = signupRequestService.createSignupRequest(
                username, email, "tempPassword", "tempName", null, null
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "가입 신청이 완료되었습니다. 이메일 인증을 확인해주세요.",
                "data", signupRequest
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 가입 신청 상태 확인
     */
    @GetMapping("/signup/status/{username}")
    public ResponseEntity<Map<String, Object>> getSignupStatus(@PathVariable String username) {
        try {
            SignupRequestDto signupRequest = signupRequestService.getSignupRequestById(
                signupRequestService
                    .getAllSignupRequests().stream()
                    .filter(r -> username.equals(r.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("가입 신청을 찾을 수 없습니다."))
                    .getId()
            );
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", signupRequest
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 가입 신청 인증 완료
     */
    @PostMapping("/signup/verify/{id}")
    public ResponseEntity<Map<String, Object>> verifySignupRequest(@PathVariable Long id) {
        try {
            SignupRequestDto verified = signupRequestService.updateSignupRequestStatus(id, "VERIFIED");
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "가입 신청이 인증되었습니다.",
                "data", verified
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 모든 가입 신청 조회 (관리자용)
     */
    @GetMapping("/signup/all")
    public ResponseEntity<Map<String, Object>> getAllSignupRequests() {
        try {
            List<SignupRequestDto> requests = signupRequestService.getAllSignupRequests();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", requests
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
