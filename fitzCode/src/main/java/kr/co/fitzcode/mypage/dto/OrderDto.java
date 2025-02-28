package kr.co.fitzcode.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private int orderId;
    private int userId;
    private int addressId;
    private int productId;
    private int couponId;
    private int totalPrice;
    private int orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
