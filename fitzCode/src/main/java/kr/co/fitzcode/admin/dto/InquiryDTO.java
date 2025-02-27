package kr.co.fitzcode.admin.dto;

import kr.co.fitzcode.common.enums.InquiryCategory;
import kr.co.fitzcode.common.enums.InquiryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryDTO {
    private int inquiryId;           // 문의 ID
    private int userId;              // 문의한 사용자 ID
    private String userName;         // 사용자 이름
    private Integer orderId;         // 관련 주문 ID (없을 수도 있어서 Integer)

    private String subject;          // 문의 제목
    private String content;          // 문의 내용

    private int categoryCode;        // 문의 카테고리 코드 (예: 1 - 배송, 2 - 환불, 3 - 기타)
    private int statusCode;          // 문의 상태 코드 (1: PENDING, 2: ANSWERED, 3: CLOSED)
    private String reply;            // 관리자 답변 내용

    private LocalDateTime createdAt; // 문의 작성 날짜
    private LocalDateTime updatedAt; // 문의 수정 날짜

    // 문의 상태 코드(int) → InquiryStatus Enum 변환
    public InquiryStatus getStatus() {
        return InquiryStatus.fromCode(statusCode);
    }

    // 문의 카테고리 코드(int) → InquiryCategory Enum 변환
    public InquiryCategory getCategory() {
        return InquiryCategory.fromCode(categoryCode);
    }
}