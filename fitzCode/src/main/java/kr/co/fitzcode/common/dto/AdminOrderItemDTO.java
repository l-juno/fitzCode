package kr.co.fitzcode.common.dto;

import lombok.Data;

@Data
public class AdminOrderItemDTO {
    private Long orderDetailId; // 주문 상세 ID
    private String productName; // 상품명
    private String description; // 상품 설명
    private Integer quantity;   // 주문 수량
    private Double unitPrice;   // 개별 상품 가격
    private Double subtotal;    // 소계 (quantity * unitPrice)
    private String imageUrl;    // 상품 이미지
}