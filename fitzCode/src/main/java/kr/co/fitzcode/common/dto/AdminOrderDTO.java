package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminOrderDTO {
    private Long orderId;           // 주문번호
    private OrderStatus orderStatus;// 주문상태
    private String productName;     // 대표 상품명
    private LocalDateTime createdAt;// 주문날짜
}