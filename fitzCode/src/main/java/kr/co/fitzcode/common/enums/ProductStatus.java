package kr.co.fitzcode.common.enums;

import java.util.Arrays;

public enum ProductStatus {
    ACTIVE(1, "활성"),
    INACTIVE(2, "비활성"),
    DISCONTINUED(3, "단종");

    private final int code;
    private final String description; // 설명 추가

    ProductStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() { // Thymeleaf에서 호출
        return description;
    }

    public static ProductStatus fromCode(int code) {
        return Arrays.stream(ProductStatus.values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 상태 code: " + code));
    }
}