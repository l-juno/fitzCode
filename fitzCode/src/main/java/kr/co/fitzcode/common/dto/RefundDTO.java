package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.common.enums.OrderStatus;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RefundDTO {
    private static final Logger log = LoggerFactory.getLogger(RefundDTO.class);

    private Long refundId;
    private Long orderId;
    private Long orderDetailId;
    private Long paymentId;
    private String refundReason;
    private RefundStatus refundStatus;
    private Integer refundAmount;
    private Integer remainingRefundAmount;
    private Integer refundMethod;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;
    private String userName;
    private String transactionId;
    private Integer paymentAmount;
    private Long userId;
    private List<OrderDetailDTO> requestedItems;
    private String accountHolder;
    private String bankName;
    private String accountNumber;
    private Integer refundStatusCode;

    public Integer getCalculatedRefundAmount() {
        if (requestedItems == null || requestedItems.isEmpty()) {
            log.debug("No requested items found or empty for refundId: {}", refundId);
            return 0;
        }
        int calculatedAmount = requestedItems.stream()
                .mapToInt(item -> {
                    int amount = (item.getRefundAmount() != null ? item.getRefundAmount() : item.getPrice() * item.getQuantity());
                    log.debug("Item - orderDetailId: {}, price: {}, quantity: {}, amount: {}",
                            item.getOrderDetailId(), item.getPrice(), item.getQuantity(), amount);
                    return amount > 0 ? amount : 0;
                })
                .sum();
        log.debug("Calculated refund amount before limit for refundId {}: {}", refundId, calculatedAmount);
        int limitedAmount = Math.min(calculatedAmount, this.paymentAmount != null ? this.paymentAmount : Integer.MAX_VALUE);
        log.debug("Limited refund amount for refundId {}: {}", refundId, limitedAmount);
        return limitedAmount;
    }

    public Integer getRequestedRefundAmount() {
        if (requestedItems == null || requestedItems.isEmpty()) {
            log.debug("No requested items for requested refund amount calculation, refundId: {}", refundId);
            return 0;
        }
        int requestedAmount = requestedItems.stream()
                .mapToInt(item -> {
                    int price = item.getPrice() != null ? item.getPrice() : 0;
                    int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    int amount = price * quantity;
                    log.debug("Requested item - orderDetailId: {}, price: {}, quantity: {}, amount: {}",
                            item.getOrderDetailId(), price, quantity, amount);
                    return amount > 0 ? amount : 0;
                })
                .sum();
        log.debug("Total requested refund amount for refundId {}: {}", refundId, requestedAmount);
        return requestedAmount;
    }

    public Integer getRefundStatusCode() {
        return refundStatus != null ? refundStatus.getCode() : null;
    }

    public void setRefundStatusCode(Integer code) {
        this.refundStatusCode = code;
        this.refundStatus = code != null ? RefundStatus.fromCode(code) : null;
    }

    public void setRefundStatusByCode(Integer code) {
        this.refundStatus = code != null ? RefundStatus.fromCode(code) : null;
    }

    public String getRefundStatusInKorean() {
        return refundStatus != null ? refundStatus.getDescription() : "알 수 없음";
    }

    @Data
    public static class OrderDetailDTO {
        private static final Logger log = LoggerFactory.getLogger(OrderDetailDTO.class);

        private Long orderId;
        private LocalDateTime orderDate;
        private String userName;
        private Integer quantity;
        private Integer totalAmount;
        private OrderStatus orderStatus;
        private Long orderDetailId;
        private Long productId;
        private String productName;
        private Integer price;
        private RefundStatus refundStatus;
        private Integer refundAmount;
        private Integer refundStatusCode;

        public Integer getOrderStatusCode() {
            return orderStatus != null ? orderStatus.getCode() : null;
        }

        public void setOrderStatusByCode(Integer code) {
            this.orderStatus = code != null ? OrderStatus.fromCode(code) : null;
        }

        public Integer getRefundStatusCode() {
            return refundStatus != null ? refundStatus.getCode() : null;
        }

        public void setRefundStatusCode(Integer code) {
            this.refundStatusCode = code;
            try {
                this.refundStatus = code != null ? RefundStatus.fromCode(code) : null;
            } catch (IllegalArgumentException e) {
                this.refundStatus = null;
                log.warn("Invalid refund status code: {} for orderDetailId: {}", code, orderDetailId, e);
            }
        }

        public void setRefundStatusByCode(Integer code) {
            this.refundStatus = code != null ? RefundStatus.fromCode(code) : null;
        }

        public String getOrderStatusInKorean() {
            return orderStatus != null ? orderStatus.getDescription() : "알 수 없음";
        }

        public String getRefundStatusInKorean() {
            return refundStatus != null ? refundStatus.getDescription() : "환불 없음";
        }
    }
}