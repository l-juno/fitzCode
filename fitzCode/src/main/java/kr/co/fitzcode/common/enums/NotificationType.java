package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    COMMENT(1, "댓글 알림"),
    LIKE(2, "좋아요 알림"),
    DELIVERY(3, "배송 알림"),
    INQUIRY(4, "문의 답변 알림"),
    FOLLOW(5, "팔로우 알림");
    private final int code;
    private final String description;

    // 코드로부터 Enum을 가져오는 메서드
    public static NotificationType fromCode(int code) {
        for (NotificationType type : NotificationType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 알림 코드: " + code);
    }
}
