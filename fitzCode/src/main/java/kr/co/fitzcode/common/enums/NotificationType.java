package kr.co.fitzcode.common.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    NOTICE(1, "공지사항"),
    SHIPPING(2, "배송 상태 변경"),
    REFUND(3, "환불 완료"),
    INQUIRY_RESPONSE(4, "1:1 문의 답변"),
    INQUIRY_CREATED(5, "1:1 문의 등록"),
    REFUND_REQUEST(6, "환불 요청"),
    PAYMENT(7, "상품 결제"),
    QNA_CREATED(8, "QnA 등록"),
    REVIEW_CREATED(9, "리뷰 등록");

    private final int code;
    private final String description;

    NotificationType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static NotificationType fromCode(int code) {
        for (NotificationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NotificationType code: " + code);
    }
}