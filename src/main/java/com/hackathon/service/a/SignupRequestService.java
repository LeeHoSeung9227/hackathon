package com.hackathon.service.a;

import com.hackathon.dto.a.SignupRequestDto;
import com.hackathon.entity.a.SignupRequest;
import com.hackathon.repository.a.SignupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupRequestService {
    
    private final SignupRequestRepository signupRequestRepository;
    
    public SignupRequestDto createSignupRequest(String username, String email, String password, String name, String college, String campus) {
        SignupRequest request = new SignupRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        request.setCollege(college);
        request.setCampus(campus);
        request.setStatus("PENDING");
        
        SignupRequest savedRequest = signupRequestRepository.save(request);
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
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}
