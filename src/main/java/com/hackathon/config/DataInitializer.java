package com.hackathon.config;

import com.hackathon.entity.a.User;
import com.hackathon.entity.b.Badge;
import com.hackathon.repository.a.UserRepository;
import com.hackathon.repository.b.BadgeRepository;
import com.hackathon.service.b.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 사용자 데이터 초기화
        if (userRepository.count() == 0) {
            initializeUsers();
        }

        // 뱃지 데이터 초기화 - BadgeService 사용
        if (badgeRepository.count() == 0) {
            badgeService.initializeDefaultBadges();
        }
    }

    private void initializeUsers() {
        // 관리자 사용자
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@hackathon.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("관리자");
        admin.setNickname("관리자");
        admin.setSchool("서울대학교");
        admin.setCampus("서울");
        admin.setLevel(1);
        admin.setPointsTotal(1000);
        admin.setCollege("공과대학");
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        userRepository.save(admin);

        // 테스트 사용자들
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@hackathon.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setName("김설호");
        user1.setNickname("설호");
        user1.setSchool("서울대학교");
        user1.setCampus("서울");
        user1.setLevel(1);
        user1.setPointsTotal(750);
        user1.setCollege("공과대학");
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@hackathon.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setName("김지수");
        user2.setNickname("지수");
        user2.setSchool("서울대학교");
        user2.setCampus("서울");
        user2.setLevel(1);
        user2.setPointsTotal(500);
        user2.setCollege("공과대학");
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("user3");
        user3.setEmail("user3@hackathon.com");
        user3.setPassword(passwordEncoder.encode("password"));
        user3.setName("이가은");
        user3.setNickname("가은");
        user3.setSchool("서울대학교");
        user3.setCampus("서울");
        user3.setLevel(1);
        user3.setPointsTotal(300);
        user3.setCollege("공과대학");
        user3.setCreatedAt(LocalDateTime.now());
        user3.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user3);

        User user4 = new User();
        user4.setUsername("user4");
        user4.setEmail("user4@hackathon.com");
        user4.setPassword(passwordEncoder.encode("password"));
        user4.setName("이호승");
        user4.setNickname("호승");
        user4.setSchool("서울대학교");
        user4.setCampus("서울");
        user4.setLevel(1);
        user4.setPointsTotal(200);
        user4.setCollege("공과대학");
        user4.setCreatedAt(LocalDateTime.now());
        user4.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user4);

        User user5 = new User();
        user5.setUsername("user5");
        user5.setEmail("user5@hackathon.com");
        user5.setPassword(passwordEncoder.encode("password"));
        user5.setName("안예영");
        user5.setNickname("예영");
        user5.setSchool("서울대학교");
        user5.setCampus("서울");
        user5.setLevel(1);
        user5.setPointsTotal(100);
        user5.setCollege("공과대학");
        user5.setCreatedAt(LocalDateTime.now());
        user5.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user5);

        User haeun = new User();
        haeun.setUsername("haeun");
        haeun.setEmail("haeun@hanyang.ac.kr");
        haeun.setPassword(passwordEncoder.encode("password"));
        haeun.setName("김하은");
        haeun.setNickname("하은");
        haeun.setSchool("한양대학교");
        haeun.setCampus("서울");
        haeun.setLevel(1);
        haeun.setPointsTotal(200);
        haeun.setCollege("디자인대학");
        haeun.setCreatedAt(LocalDateTime.now());
        haeun.setUpdatedAt(LocalDateTime.now());
        userRepository.save(haeun);
    }
}
