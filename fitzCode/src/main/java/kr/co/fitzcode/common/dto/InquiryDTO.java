package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.InquiryCategory;
import kr.co.fitzcode.common.enums.InquiryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class InquiryDTO {
    private int inquiryId;           // 문의 ID
    private int userId;              // 문의한 사용자 ID
    private String userName;         // 사용자 이름
    private Integer orderId;         // 관련 주문 ID (없을 수도 있어서 Integer)

    private String subject;          // 문의 제목
    private String content;          // 문의 내용

    private int categoryCode;        // 문의 카테고리 코드 (1 - 일반, 2 - 상품, 3 - 환불)
    private int statusCode;          // 문의 상태 코드 (1: PENDING, 2: ANSWERED, 3: CLOSED)
    private String reply;            // 관리자 답변 내용

    private LocalDateTime createdAt; // 문의 작성 날짜
    private LocalDateTime updatedAt; // 문의 수정 날짜

    private Integer productId;       // 관련 상품 ID (없을 수도 있어서 Integer)
    private String productName;      // 상품 이름

    private List<String> imageUrls;  // 문의 시 사용자가 첨부한 이미지 (최대 5개 제한)

    // 문의 상태 코드(int) → InquiryStatus Enum 변환
    public InquiryStatus getStatus() {
        return InquiryStatus.fromCode(statusCode);
    }

    // 문의 카테고리 코드(int) → InquiryCategory Enum 변환
    public InquiryCategory getCategory() {
        return InquiryCategory.fromCode(categoryCode);
    }

}