package com.hackathon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * userId를 다양한 타입에서 Long으로 파싱하는 유틸리티 클래스
 * 정수형, 문자열, Object 등 모든 타입을 지원
 */
@Component
public class UserIdParser {
    
    private static final Logger log = LoggerFactory.getLogger(UserIdParser.class);

    /**
     * Object 타입의 userId를 Long으로 파싱
     * @param userIdObj userId 객체 (String, Integer, Long, Object 등)
     * @return 파싱된 Long userId
     * @throws IllegalArgumentException 파싱할 수 없는 경우
     */
    public static Long parseUserId(Object userIdObj) {
        if (userIdObj == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }

        try {
            if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                String userIdStr = (String) userIdObj;
                if (userIdStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("userId는 빈 문자열일 수 없습니다.");
                }
                return Long.parseLong(userIdStr.trim());
            } else if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            } else {
                // Object의 toString()을 시도
                String userIdStr = userIdObj.toString();
                if (userIdStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("userId는 빈 문자열일 수 없습니다.");
                }
                return Long.parseLong(userIdStr.trim());
            }
        } catch (NumberFormatException e) {
            log.error("userId 파싱 실패: {} (타입: {})", userIdObj, userIdObj.getClass().getSimpleName());
            throw new IllegalArgumentException("유효하지 않은 userId 형식입니다: " + userIdObj);
        } catch (Exception e) {
            log.error("userId 파싱 중 예상치 못한 오류: {} (타입: {})", userIdObj, userIdObj.getClass().getSimpleName(), e);
            throw new IllegalArgumentException("userId 파싱 중 오류가 발생했습니다: " + userIdObj);
        }
    }

    /**
     * Object 타입의 userId를 Long으로 파싱 (기본값 포함)
     * @param userIdObj userId 객체
     * @param defaultValue 파싱 실패 시 반환할 기본값
     * @return 파싱된 Long userId 또는 기본값
     */
    public static Long parseUserId(Object userIdObj, Long defaultValue) {
        try {
            return parseUserId(userIdObj);
        } catch (Exception e) {
            log.warn("userId 파싱 실패, 기본값 사용: {} -> {}", userIdObj, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Object 타입의 userId를 Long으로 파싱 (null 허용)
     * @param userIdObj userId 객체
     * @return 파싱된 Long userId 또는 null
     */
    public static Long parseUserIdOrNull(Object userIdObj) {
        if (userIdObj == null) {
            return null;
        }
        try {
            return parseUserId(userIdObj);
        } catch (Exception e) {
            log.warn("userId 파싱 실패, null 반환: {}", userIdObj);
            return null;
        }
    }

    /**
     * userId가 유효한지 검증
     * @param userId 검증할 userId
     * @return 유효한 경우 true
     */
    public static boolean isValidUserId(Long userId) {
        return userId != null && userId > 0;
    }

    /**
     * Object 타입의 userId가 유효한지 검증
     * @param userIdObj 검증할 userId 객체
     * @return 유효한 경우 true
     */
    public static boolean isValidUserId(Object userIdObj) {
        try {
            Long userId = parseUserId(userIdObj);
            return isValidUserId(userId);
        } catch (Exception e) {
            return false;
        }
    }
}
