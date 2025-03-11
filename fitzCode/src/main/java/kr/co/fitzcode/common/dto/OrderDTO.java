package kr.co.fitzcode.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int orderId;             // 주문번호
    private int userId;
    private int addressId;
    private int totalPrice;
    private int orderStatus;         // 주문 상태 코드
    private LocalDateTime createdAt; // 주문일
    private LocalDateTime updatedAt;
}