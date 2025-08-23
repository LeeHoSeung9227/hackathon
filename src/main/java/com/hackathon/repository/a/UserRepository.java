package com.hackathon.repository.a;

import com.hackathon.entity.a.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByCollege(String college);
    List<User> findByNameContainingIgnoreCase(String name);  // 이름으로 검색 (대소문자 무시)
}
