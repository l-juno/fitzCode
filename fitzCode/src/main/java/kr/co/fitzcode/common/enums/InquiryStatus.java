package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryStatus {
    PENDING(1, "대기 중"),
    ANSWERED(2, "답변 완료"),
    CLOSED(3, "종료");

    private final int code;
    private final String description;

    public static InquiryStatus fromCode(int code) {
        for (InquiryStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 문의 상태 코드: " + code);
    }
}