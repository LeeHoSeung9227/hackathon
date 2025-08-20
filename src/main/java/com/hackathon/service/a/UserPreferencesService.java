package com.hackathon.service.a;

import com.hackathon.dto.a.UserPreferencesDto;
import com.hackathon.entity.a.UserPreferences;
import com.hackathon.repository.a.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesDto createUserPreference(Long userId, String preferenceType, String preferenceValue) {
        UserPreferences preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setPreferenceType(preferenceType);
        preference.setPreferenceValue(preferenceValue);

        UserPreferences savedPreference = userPreferencesRepository.save(preference);
        return convertToDto(savedPreference);
    }

    public UserPreferencesDto getUserPreferenceById(Long id) {
        UserPreferences preference = userPreferencesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 선호도를 찾을 수 없습니다: " + id));
        return convertToDto(preference);
    }

    public List<UserPreferencesDto> getAllUserPreferences() {
        return userPreferencesRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserPreferencesDto> getUserPreferencesByUserId(Long userId) {
        return userPreferencesRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserPreferencesDto> getUserPreferenceByUserIdAndType(Long userId, String preferenceType) {
        return userPreferencesRepository.findByUserIdAndPreferenceType(userId, preferenceType)
                .map(this::convertToDto);
    }

    public List<UserPreferencesDto> getUserPreferencesByType(String preferenceType) {
        return userPreferencesRepository.findByPreferenceTypeOrderByCreatedAtDesc(preferenceType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserPreferencesDto updateUserPreference(Long id, String preferenceValue) {
        UserPreferences preference = userPreferencesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 선호도를 찾을 수 없습니다: " + id));

        preference.setPreferenceValue(preferenceValue);

        UserPreferences updatedPreference = userPreferencesRepository.save(preference);
        return convertToDto(updatedPreference);
    }

    public void deleteUserPreference(Long id) {
        if (!userPreferencesRepository.existsById(id)) {
            throw new RuntimeException("사용자 선호도를 찾을 수 없습니다: " + id);
        }
        userPreferencesRepository.deleteById(id);
    }

    private UserPreferencesDto convertToDto(UserPreferences preference) {
        UserPreferencesDto dto = new UserPreferencesDto();
        dto.setId(preference.getId());
        dto.setUserId(preference.getUserId());
        dto.setPreferenceType(preference.getPreferenceType());
        dto.setPreferenceValue(preference.getPreferenceValue());
        dto.setCreatedAt(preference.getCreatedAt());
        dto.setUpdatedAt(preference.getUpdatedAt());
        return dto;
    }
}
