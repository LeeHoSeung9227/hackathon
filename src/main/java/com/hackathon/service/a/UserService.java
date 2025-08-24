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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserDto createUser(String username, String email, String password, String name, String college, String campus) {
        return createUser(username, email, password, name, null, null, college, campus);
    }
    
    public UserDto createUser(String username, String email, String password, String name, String nickname, String school, String college, String campus) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setNickname(nickname);
        user.setSchool(school);
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
    
    /**
     * 사용자 포인트 업데이트 (추가/차감)
     */
    public void updateUserPoints(Long userId, Integer points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        int newTotalPoints = user.getPointsTotal() + points;
        if (newTotalPoints < 0) {
            newTotalPoints = 0; // 포인트는 음수가 될 수 없음
        }
        
        user.setPointsTotal(newTotalPoints);
        
        // 레벨업 체크
        int newLevel = calculateLevel(newTotalPoints);
        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);
        }
        
        userRepository.save(user);
    }
    
    /**
     * 단과대 총 포인트 계산
     */
    public int getCollegeTotalPoints(String college) {
        if (college == null) return 0;
        
        return userRepository.findByCollege(college).stream()
                .mapToInt(User::getPointsTotal)
                .sum();
    }
    
    /**
     * 이름으로 사용자 검색
     */
    public List<UserDto> searchUsersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        
        return userRepository.findByNameContainingIgnoreCase(name.trim()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 닉네임, 학교명, 단과대로 사용자 검색
     */
    public User searchUserByNicknameAndSchoolAndCollege(String nickname, String school, String college) {
        log.info("사용자 검색: nickname={}, school={}, college={}", nickname, school, college);
        
        try {
            // 간단한 방식으로 사용자 검색
            List<User> allUsers = userRepository.findAll();
            User foundUser = allUsers.stream()
                    .filter(user -> nickname.equals(user.getNickname()) &&
                                   school.equals(user.getSchool()) &&
                                   college.equals(user.getCollege()))
                    .findFirst()
                    .orElse(null);
            
            if (foundUser != null) {
                log.info("사용자 검색 성공: userId={}", foundUser.getId());
            } else {
                log.info("사용자를 찾을 수 없음");
            }
            
            return foundUser;
        } catch (Exception e) {
            log.error("사용자 검색 중 오류 발생", e);
            throw new RuntimeException("사용자 검색 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 포인트에 따른 레벨 계산
     */
    private int calculateLevel(int points) {
        if (points >= 1000) return 5;      // 나무
        if (points >= 500) return 4;       // 큰 새싹
        if (points >= 200) return 3;       // 새싹
        if (points >= 100) return 2;       // 작은 새싹
        return 1;                          // 씨앗
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setNickname(user.getNickname());
        dto.setSchool(user.getSchool());
        dto.setLevel(user.getLevel());
        dto.setPointsTotal(user.getPointsTotal());
        dto.setCollege(user.getCollege());
        dto.setCampus(user.getCampus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
