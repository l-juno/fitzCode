package kr.co.fitzcode.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryDTO {
    private int inquiryId;
    private int userId;                     // 사용자 아이디 (공통)
    private Integer orderId;                // 주문번호 (null 값 허용) (환불, 결제, 반품)
    private String subject;                 // 문의 제목 (공통)
    private String content;                 // 문의 내용 (공통)
    private int category;               // 문의 종류 (공통) (1: 일반문의, 2: 상품문의, 3: 환불문의, 4: 결제문의, 5: 반품문의)
    private int status;                     // 문의 상태 (공통) (1: PENDING, 2: ANSWERED, 3: CLOSED)
    private String reply;                   // 관리자 답변 (Null 가능)
    private LocalDateTime createdAt;        // 문의 생성 날짜
    private LocalDateTime updatedAt;        // 문의 수정 날짜
    private Integer productId;              // 상품 ID (상품, 일반)
    private List<MultipartFile> images;     // 문의 이미지 (환불, 반품, 일반)


}