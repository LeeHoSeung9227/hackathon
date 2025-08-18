package com.hackathon.repository.a;

import com.hackathon.entity.a.AuthLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthLoginRepository extends JpaRepository<AuthLogin, Long> {
    Optional<AuthLogin> findByToken(String token);
    Optional<AuthLogin> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
