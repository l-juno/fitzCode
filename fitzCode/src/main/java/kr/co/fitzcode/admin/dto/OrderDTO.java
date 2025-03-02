package kr.co.fitzcode.admin.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private int orderId;             // 주문번호
    private LocalDateTime createdAt; // 주문일
    private int orderStatus;         // 주문 상태 코드
}