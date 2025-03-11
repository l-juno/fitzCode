package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;

import java.util.List;

public interface AdminRefundService {

    // 환불 목록 조회 (페이징)
    List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId);

    // 특정 환불 상세 조회
    RefundDTO getRefundDetail(Long refundId);

    // 환불 상태 및 환불 금액 업데이트
    void updateRefundStatus(Long refundId, RefundStatus status, Integer refundAmount);

    // 환불 후 남은 환불 가능 금액 업데이트
    void updateRemainingRefundAmount(Long refundId, int remainingAmount);

    // 주문 상세 내역의 환불 상태 변경
    void updateOrderDetailRefundStatus(Long orderDetailId, int status);

    // 환불 트랜잭션 로그 기록
    void logTransaction(RefundDTO refund);

    // 사용자에게 환불 완료 알림 전송
    void sendNotification(RefundDTO refund);

    // 환불 건수 조회
    int getRefundCount(Integer status, String orderId);
}