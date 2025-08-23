package com.hackathon.service.a;

import com.hackathon.dto.a.UserDto;
import com.hackathon.entity.a.User;
import com.hackathon.repository.a.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserDto createUser(String username, String email, String password, String name, String college, String campus) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setCollege(college);
        user.setCampus(campus);
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        return convertToDto(user);
    }
    
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        return convertToDto(user);
    }
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        user.setName(userDto.getName());
        user.setCollege(userDto.getCollege());
        user.setCampus(userDto.getCampus());
        user.setLevel(userDto.getLevel());
        user.setPointsTotal(userDto.getPointsTotal());
        
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * 사용자 인증 (로그인용)
     */
    public Optional<UserDto> authenticateUser(String username, String password) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElse(null);
            
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(convertToDto(user));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLevel(user.getLevel());
        dto.setPointsTotal(user.getPointsTotal());
        dto.setCollege(user.getCollege());
        dto.setCampus(user.getCampus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
