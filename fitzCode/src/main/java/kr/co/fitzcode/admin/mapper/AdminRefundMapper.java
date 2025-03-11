package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.common.dto.RefundDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRefundMapper {

    // 환불 목록 조회 (페이징)
    List<RefundDTO> getRefundList(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("status") Integer status,
            @Param("orderId") String orderId
    );

    // 환불 상세 조회
    RefundDTO getRefundDetail(@Param("refundId") Long refundId);

    // 환불 상태 업데이트
    void updateRefundStatus(@Param("refundId") Long refundId, @Param("status") int status);

    // 남은 환불 가능 금액 업데이트
    void updateRemainingRefundAmount(@Param("refundId") Long refundId, @Param("remainingAmount") int remainingAmount);

    // 주문 상세 내역의 환불 상태 변경
    void updateOrderDetailRefundStatus(@Param("orderDetailId") Long orderDetailId, @Param("status") int status);

    // 환불 트랜잭션 로그 저장용 (환불 내역 저장)
    void insertSalesTransactionLog(
            @Param("orderId") Long orderId,
            @Param("orderDetailId") Long orderDetailId,
            @Param("productId") Long productId,
            @Param("userId") Long userId,
            @Param("amount") int amount,
            @Param("transactionType") int transactionType
    );

    // 사용자에게 환불 관련 알림 전송
    void insertNotification(
            @Param("userId") Long userId,
            @Param("type") int type,
            @Param("message") String message,
            @Param("relatedId") Long relatedId
    );

    // 환불 건수 조회 (필터)
    int getRefundCount(@Param("status") Integer status, @Param("orderId") String orderId);

    // 환불 금액 업데이트
    void updateRefundAmount(@Param("refundId") Long refundId, @Param("refundAmount") int refundAmount);
}