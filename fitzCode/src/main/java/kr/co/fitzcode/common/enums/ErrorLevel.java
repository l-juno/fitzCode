package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorLevel {
    WARNING(1, "WARNING"),
    ERROR(2, "ERROR"),
    CRITICAL(3, "CRITICAL");

    private final int code;
    private final String description;

    // 코드로 ENUM 반환
    public static ErrorLevel fromCode(int code) {
        for (ErrorLevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 에러 코드: " + code);
    }

    // 코드로 설명 반환
    public static String getDescription(int code) {
        try {
            return fromCode(code).getDescription();
        } catch (IllegalArgumentException e) {
            return "UNKNOWN";
        }
    }
}