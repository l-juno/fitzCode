package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER(1, "사용자"),
    ADMIN(2, "관리자"),
    LOGISTICS(3, "물류담당자"),
    INQUIRY(4, "문의담당자");

    private final int code;
    private final String description;

    public static UserRole fromCode(int code) {
        for (UserRole role : values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 역할 코드: " + code);
    }
}