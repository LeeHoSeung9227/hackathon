package com.hackathon.service;

import com.hackathon.dto.SignupRequestDto;
import com.hackathon.entity.SignupRequest;
import com.hackathon.repository.SignupRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SignupRequestService {
    
    private final SignupRequestRepository signupRequestRepository;
    
    public SignupRequestService(SignupRequestRepository signupRequestRepository) {
        this.signupRequestRepository = signupRequestRepository;
    }
    
    /**
     * 새로운 가입 신청 생성
     */
    public SignupRequestDto createSignupRequest(String username, String email) {
        // 중복 사용자명 체크
        if (signupRequestRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        
        // 중복 이메일 체크
        if (signupRequestRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setEmail(email);
        signupRequest.setIsVerified(false);
        signupRequest.setCreatedAt(LocalDateTime.now());
        
        SignupRequest saved = signupRequestRepository.save(signupRequest);
        return convertToDto(saved);
    }
    
    /**
     * 사용자명으로 가입 신청 조회
     */
    public Optional<SignupRequestDto> getSignupRequestByUsername(String username) {
        return signupRequestRepository.findByUsername(username)
                .map(this::convertToDto);
    }
    
    /**
     * 가입 신청 인증
     */
    public SignupRequestDto verifySignupRequest(Long id) {
        SignupRequest signupRequest = signupRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("가입 신청을 찾을 수 없습니다."));
        
        signupRequest.setIsVerified(true);
        SignupRequest saved = signupRequestRepository.save(signupRequest);
        return convertToDto(saved);
    }
    
    /**
     * 모든 가입 신청 조회
     */
    public List<SignupRequestDto> getAllSignupRequests() {
        return signupRequestRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 인증 상태별 가입 신청 조회
     */
    public List<SignupRequestDto> getSignupRequestsByVerificationStatus(Boolean isVerified) {
        return signupRequestRepository.findByIsVerified(isVerified)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 가입 신청 삭제
     */
    public void deleteSignupRequest(Long id) {
        signupRequestRepository.deleteById(id);
    }
    
    /**
     * DTO 변환
     */
    private SignupRequestDto convertToDto(SignupRequest signupRequest) {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setId(signupRequest.getId());
        dto.setUsername(signupRequest.getUsername());
        dto.setEmail(signupRequest.getEmail());
        dto.setIsVerified(signupRequest.getIsVerified());
        dto.setCreatedAt(signupRequest.getCreatedAt());
        return dto;
    }
}
