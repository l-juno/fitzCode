package kr.co.fitzcode.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "주문 정보")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @Schema(description = "주문번호")
    private int orderId;

    @Schema(description = "주문일")
    private LocalDateTime createdAt;

    @Schema(description = "주문 상태 코드")
    private int orderStatus;

    private int addressId;

    @Schema(description = "사용자 아이디")
    private int userId;

    @Schema(description = "총 주문 금액")
    private Integer totalPrice;

    @Schema(description = "운송장 번호")
    private String trackingNumber;

    @Schema(description = "배송 상태")
    private int deliveryStatus;

    @Schema(description = "발송 날짜")
    private LocalDateTime shippedAt;

    @Schema(description = "배송 완료 날짜")
    private LocalDateTime deliveredAt;

    public String getFormattedTotalPrice() {
        DecimalFormat df = new DecimalFormat("#,###원");
        return df.format(totalPrice);
    }

    public OrderStatus getStatus() {
        return OrderStatus.fromCode(orderStatus);
    }
}