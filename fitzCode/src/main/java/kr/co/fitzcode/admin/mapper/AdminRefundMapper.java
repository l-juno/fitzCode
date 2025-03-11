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
            @Param("productId") Long productId,
            @Param("userId") Long userId,
            @Param("amount") int amount,
            @Param("transactionType") int transactionType
    );

    void insertNotification(
            @Param("userId") Long userId,
            @Param("type") int type,
            @Param("message") String message,
            @Param("relatedId") Long relatedId
    );

    int getRefundCount(@Param("status") Integer status, @Param("orderId") String orderId);
}