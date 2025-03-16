package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private int orderId;             // 주문번호
    private LocalDateTime createdAt; // 주문일
    private int orderStatus;         // 주문 상태 코드

    // 추가
    private int userId;             // 사용자 아이디
    private Integer totalPrice;         // 총 주문 금액
    private String trackingNumber;  // 운송장 번호
    private int deliveryStatus;     // 배송 상태
    private LocalDateTime shippedAt;// 발송 날짜
    private LocalDateTime deliveryAt;// 배송 완료 날짜

    // 총 주문 금액 포맷팅
    public String getFormattedTotalPrice() {
        DecimalFormat df = new DecimalFormat("#,###원");
        return df.format(totalPrice);
    }

    // 주문 상태코드 (int) -> OrderStatus Enum 변환
    public OrderStatus getStatus() {
        return OrderStatus.fromCode(orderStatus);
    }
}