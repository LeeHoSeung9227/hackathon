package com.hackathon.repository.a;

import com.hackathon.entity.a.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SignupRequestRepository extends JpaRepository<SignupRequest, Long> {
    Optional<SignupRequest> findByUsername(String username);
    Optional<SignupRequest> findByEmail(String email);
    List<SignupRequest> findByStatus(String status);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
