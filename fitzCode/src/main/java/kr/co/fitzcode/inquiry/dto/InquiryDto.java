package kr.co.fitzcode.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryDto {
    private int inquiryId;
    private int userId;
    private Integer orderId;                // 주문번호 (null 값 허용)
    private String subject;                 // 문의 제목
    private String content;                 // 문의 내용
    private int category;                   // 문의 종류 (1: 일반문의, 2: 상품문의, 3: 환불문의, 4: 결제문의, 5: 반품문의)
    private int status;                     // 문의 상태 (1: PENDING, 2: ANSWERED, 3: CLOSED)
    private String reply;                   // 관리자 답변 (Null 가능)
    private LocalDateTime createdAt;        // 문의 생성 날짜
    private LocalDateTime updatedAt;        // 문의 수정 날짜
    private Integer productId;              // 상품 ID
    private List<InquiryImageDto> images;   // 문의 이미지
}
