package com.hackathon.repository.a;

import com.hackathon.entity.a.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    List<UserPreferences> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<UserPreferences> findByUserIdAndPreferenceType(Long userId, String preferenceType);
    List<UserPreferences> findByPreferenceTypeOrderByCreatedAtDesc(String preferenceType);
}
