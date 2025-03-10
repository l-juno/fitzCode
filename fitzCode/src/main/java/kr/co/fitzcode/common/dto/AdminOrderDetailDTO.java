package kr.co.fitzcode.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminOrderDetailDTO {
    private Long orderId;               // 주문 ID
    private Integer orderStatus;        // 주문 상태
    private LocalDateTime createdAt;    // 주문 생성일
    private Double totalAmount;         // 총 주문 금액
    private Long userId;                // 주문자 ID
    private String username;            // 주문자 이름
    private String email;               // 주문자 이메일
    private List<AdminOrderItemDTO> items; // 주문 상품 목록
    private Integer deliveryStatus;     // 배송 상태
    private String trackingNumber;      // 운송장 번호
    private String address;             // 배송지 주소
    private LocalDateTime shippedAt;    // 배송 시작일
    private LocalDateTime deliveredAt;  // 배송 완료일
    private Integer paymentMethod;      // 결제 수단
    private String bankName;            // 은행명
    private String accountNumber;       // 계좌 번호
    private String accountHolder;       // 예금주
}