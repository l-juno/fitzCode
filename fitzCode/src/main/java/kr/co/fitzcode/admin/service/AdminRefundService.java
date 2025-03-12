package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;

import java.util.List;

public interface AdminRefundService {
    List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId);
    RefundDTO getRefundDetail(Long refundId);
    void updateRefundStatus(Long refundId, RefundStatus status, Integer refundAmount);
    void updateRemainingRefundAmount(Long refundId, int remainingAmount);
    void updateOrderDetailRefundStatus(Long orderDetailId, int status);
    void logTransaction(RefundDTO refund);
    void sendNotification(RefundDTO refund, String message);
    int getRefundCount(Integer status, String orderId);
    void createRefund(Long orderId, String refundReason, Integer refundMethod);
}