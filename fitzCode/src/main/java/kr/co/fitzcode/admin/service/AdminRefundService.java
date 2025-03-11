package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;

import java.util.List;

public interface AdminRefundService {

    // 환불 요청 목록을 조회 (페이지네이션 및 필터링 지원)
    List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId);

    // 특정 환불 요청의 상세 정보를 조회
    RefundDTO getRefundDetail(Long refundId);

    // 환불 상태를 업데이트
    // 상태 변경 시 카드 결제인 경우 포트원 API를 통해 결제 취소를 요청
    // 부분 취소 및 묶음 상품 환불을 위해 order_detail_id와 remaining_refund_amount를 관리
    void updateRefundStatus(Long refundId, RefundStatus status);

    // 남은 환불 가능 금액을 업데이트 (부분 취소 지원)
    void updateRemainingRefundAmount(Long refundId, int remainingAmount);

    // ORDER_DETAIL의 환불 상태를 업데이트 (개별 상품 환불 처리)
    void updateOrderDetailRefundStatus(Long orderDetailId, int status);

    // SALES_TRANSACTION_LOG에 환불 기록 추가
    void logTransaction(RefundDTO refund);

    // NOTIFICATION에 환불 완료 알림 추가
    void sendNotification(RefundDTO refund);

    // 총 환불 건수 조회
    int getRefundCount(Integer status, String orderId);
}