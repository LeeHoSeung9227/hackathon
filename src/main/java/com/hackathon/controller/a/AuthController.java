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
            String name = request.get("name");
            String school = request.get("school");
            String college = request.get("college");
            
            if (name == null || school == null || college == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "이름, 학교명, 단과대명은 필수입니다."));
            }
            
            // 이름 + 학교 + 단과대명으로 사용자 인증
            var user = userService.findUserByNameAndSchoolAndCollege(name, school, college);
            if (user.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "해당 정보로 사용자를 찾을 수 없습니다."));
            }
            
            // 로그인 세션 생성
            AuthLoginDto loginSession = authLoginService.createLoginSession(user.get().getId());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인이 성공했습니다.",
                "data", Map.of(
                    "userId", user.get().getId(),
                    "username", user.get().getUsername(),
                    "session", loginSession
                )
            ));
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
     * 회원가입 신청 (통합 로그인/회원가입용)
     */
    @PostMapping("/signup/request")
    public ResponseEntity<Map<String, Object>> createSignupRequest(@RequestBody Map<String, String> request) {
        try {
            String nickname = request.get("nickname");
            String school = request.get("school");
            String college = request.get("college");
            String campus = request.get("campus");
            String name = request.get("name");
            
            if (nickname == null || school == null || college == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "닉네임, 학교명, 단과대는 필수입니다."));
            }
            
            // 자동으로 username과 email 생성
            String username = nickname + "_" + System.currentTimeMillis();
            String email = nickname + "@" + school.replaceAll("[^a-zA-Z0-9]", "") + ".edu";
            String password = "default123"; // 기본 비밀번호
            
            // 회원가입 신청 및 User 엔티티 생성
            SignupRequestDto signupRequest = signupRequestService.createSignupRequest(
                username, email, password, name != null ? name : nickname, nickname, school, college, campus != null ? campus : school
            );
            
            // User 엔티티도 직접 생성 (회원가입 신청과 별도로)
            var user = userService.createUser(username, email, password, name != null ? name : nickname, nickname, school, college, campus != null ? campus : school);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입이 완료되었습니다!",
                "data", Map.of(
                    "userId", user != null ? user.getId() : signupRequest.getUserId(),
                    "nickname", nickname,
                    "school", school,
                    "college", college,
                    "message", "이제 이 userId로 로그인할 수 있습니다."
                )
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
