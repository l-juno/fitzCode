package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.RefundDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRefundMapper {
    List<RefundDTO> getRefundList(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("status") Integer status,
            @Param("orderId") String orderId
    );

    RefundDTO getRefundDetail(@Param("refundId") Long refundId);

    void updateRefundStatus(@Param("refundId") Long refundId, @Param("status") int status);

    void updateRemainingRefundAmount(@Param("refundId") Long refundId, @Param("remainingAmount") int remainingAmount);

    void updateOrderDetailRefundStatus(@Param("orderDetailId") Long orderDetailId, @Param("status") int status);

    void insertSalesTransactionLog(
            @Param("orderId") Long orderId,
            @Param("orderDetailId") Long orderDetailId,
            @Param("userId") Long userId,
            @Param("amount") Long amount,
            @Param("transactionType") int transactionType
    );

    void insertNotification(
            @Param("userId") Long userId,
            @Param("type") int type,
            @Param("message") String message,
            @Param("relatedId") Long relatedId
    );

    int getRefundCount(@Param("status") Integer status, @Param("orderId") String orderId);

    void updateRefundAmount(@Param("refundId") Long refundId, @Param("refundAmount") int refundAmount);

    List<RefundDTO.OrderDetailDTO> getRequestedRefundItems(@Param("orderId") Long orderId);

    // 추가: 환불 기록 삽입
    void insertRefund(@Param("refund") RefundDTO refund);

    // 추가: order_id 기준으로 환불 상세 조회
    RefundDTO getRefundDetailByOrderId(@Param("orderId") Long orderId);

    // 추가: order_id 기준으로 결제 금액 조회
    Integer getPaymentAmountByOrderId(@Param("orderId") Long orderId);
}