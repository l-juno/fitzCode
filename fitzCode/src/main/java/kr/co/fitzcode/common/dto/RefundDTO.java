package kr.co.fitzcode.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundDTO {
    private Long refundId;
    private Long orderId;
    private Long paymentId;
    private String refundReason;
    private Integer refundStatus;
    private Integer refundAmount;
    private Integer refundMethod;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;
    private String userName;
}