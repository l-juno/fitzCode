package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum UserRole implements GrantedAuthority {
    USER(1, "사용자", "ROLE_USER"),
    ADMIN(2, "관리자", "ROLE_ADMIN"),
    LOGISTICS(3, "물류담당자", "ROLE_LOGISTICS"),
    INQUIRY(4, "문의담당자", "ROLE_INQUIRY");

    private final int code;
    private final String description;
    private final String roleName;

    public static UserRole fromCode(int code) {
        for (UserRole role : values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 역할 코드: " + code);
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}