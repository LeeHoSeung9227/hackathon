package com.hackathon.service.a;

import com.hackathon.dto.a.SignupRequestDto;
import com.hackathon.entity.a.SignupRequest;
import com.hackathon.entity.a.User;
import com.hackathon.repository.a.SignupRequestRepository;
import com.hackathon.repository.a.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupRequestService {
    
    private final SignupRequestRepository signupRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public SignupRequestDto createSignupRequest(String username, String email, String password, String name, String college, String campus) {
        // 0. 중복 가입 검증
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + email);
        }
        
        // 1. SignupRequest 생성
        SignupRequest request = new SignupRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        request.setCollege(college);
        request.setCampus(campus);
        request.setStatus("PENDING");
        
        SignupRequest savedRequest = signupRequestRepository.save(request);
        
        // 2. 바로 User 엔티티도 생성
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
        user.setName(name);
        user.setCollege(college);
        user.setCampus(campus);
        user.setLevel(1);
        user.setPointsTotal(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // 3. SignupRequest에 userId 연결
        savedRequest.setUserId(savedUser.getId());
        signupRequestRepository.save(savedRequest);
        
        return convertToDto(savedRequest);
    }
    
    public SignupRequestDto getSignupRequestById(Long id) {
        SignupRequest request = signupRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("가입 신청을 찾을 수 없습니다: " + id));
        return convertToDto(request);
    }
    
    public List<SignupRequestDto> getAllSignupRequests() {
        return signupRequestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<SignupRequestDto> getSignupRequestsByStatus(String status) {
        return signupRequestRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public SignupRequestDto updateSignupRequestStatus(Long id, String status) {
        SignupRequest request = signupRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("가입 신청을 찾을 수 없습니다: " + id));
        
        request.setStatus(status);
        SignupRequest updatedRequest = signupRequestRepository.save(request);
        return convertToDto(updatedRequest);
    }
    
    public void deleteSignupRequest(Long id) {
        if (!signupRequestRepository.existsById(id)) {
            throw new RuntimeException("가입 신청을 찾을 수 없습니다: " + id);
        }
        signupRequestRepository.deleteById(id);
    }
    
    private SignupRequestDto convertToDto(SignupRequest request) {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setId(request.getId());
        dto.setUsername(request.getUsername());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setName(request.getName());
        dto.setCollege(request.getCollege());
        dto.setCampus(request.getCampus());
        dto.setStatus(request.getStatus());
        dto.setUserId(request.getUserId());  // userId 추가
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}
