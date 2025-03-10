package kr.co.fitzcode.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private String userName;
    private Integer quantity;
    private Integer totalAmount;
    private String orderStatus;

    public String getOrderStatusInKorean() {
        switch (orderStatus) {
            case "PLACED": return "주문 접수";
            case "SHIPPED": return "배송 중";
            case "DELIVERED": return "배송 완료";
            case "CANCELLED": return "주문 취소";
            case "UNKNOWN": return "알 수 없음";
            default: return "알 수 없음";
        }
    }
}