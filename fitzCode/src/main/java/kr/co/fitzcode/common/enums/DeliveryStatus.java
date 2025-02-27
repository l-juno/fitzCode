package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    PENDING(1, "배송 준비 중"),
    IN_TRANSIT(2, "배송 중"),
    DELIVERED(3, "배송 완료"),
    RETURNED(4, "반품 완료");

    private final int code;
    private final String description;

    public static DeliveryStatus fromCode(int code) {
        for (DeliveryStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 운송장 코드: " + code);
    }
}