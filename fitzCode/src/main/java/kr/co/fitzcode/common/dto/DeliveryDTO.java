package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryDTO {
    private Long deliveryId;
    private Long orderId;
    private String trackingNumber;
    private Integer courierCode;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;

    public DeliveryDTO() {
        this.courierCode = 1; // CJ대한통운 고정
    }

    public void setCourierCode(Integer courierCode) {
        // 고정값 유지
    }
}