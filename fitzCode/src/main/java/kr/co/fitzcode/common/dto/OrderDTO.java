package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

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

    public String getOrderStatusTranslated() {
        return OrderStatus.fromCode(orderStatus).getDescription();
    }


    public String getFormattedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(totalPrice);
    }





}