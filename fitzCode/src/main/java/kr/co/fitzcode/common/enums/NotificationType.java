package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NotificationType {
    COMMENT(1, "댓글 알림"),
    LIKE(2, "좋아요 알림"),
    FOLLOW(3, "팔로우 알림"),
    MENTION(4, "멘션 알림"),
    DELIVERY_UPDATE(5, "배송 상태 업데이트"),
    ORDER_CONFIRMATION(6, "주문 확인"),
    SYSTEM_ALERT(7, "시스템 알림");

    private final int code;
    private final String description;

    public static NotificationType fromCode(int code) {
        return Arrays.stream(NotificationType.values())
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 알림 유형 코드: " + code));
    }
}