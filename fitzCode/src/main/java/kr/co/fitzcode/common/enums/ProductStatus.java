package kr.co.fitzcode.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ACTIVE(1, "판매 중"),
    SOLD_OUT(2, "품절"),
    INACTIVE(3, "비활성");

    private final int code;
    private final String description;

    public static ProductStatus fromCode(int code) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("잘못된 상태 코드입니다: " + code);
    }

    public static ProductStatus fromDescription(String description) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("잘못된 상태 설명 : " + description);
    }
}