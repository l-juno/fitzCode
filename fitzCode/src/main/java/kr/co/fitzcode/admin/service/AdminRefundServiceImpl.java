package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminRefundMapper;
import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRefundServiceImpl implements AdminRefundService {

    private final AdminRefundMapper refundMapper;
    private static final int PAGE_SIZE = 10;

    // 환불 목록 조회 (페이징 지원)
    @Override
    public List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId) {
        if (page < 1) page = 1;
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        int offset = (page - 1) * size;
        log.info("환불 목록 조회 : 페이지: {}, 개수: {}, 상태: {}, 주문 ID: {}", page, size, status, orderId);
        return refundMapper.getRefundList(size, offset, status, orderId);
    }

    // 특정 환불 상세 정보 조회
    @Override
    public RefundDTO getRefundDetail(Long refundId) {
        log.info("환불 상세 조회 - 환불 ID: {}", refundId);
        RefundDTO refund = refundMapper.getRefundDetail(refundId);
        if (refund == null) {
            log.warn("환불 기록 없음 - 환불 ID: {}", refundId);
            throw new RuntimeException("환불 기록을 찾을 수 없음");
        }
        return refund;
    }

    // 환불 상태 업데이트
    @Override
    @Transactional
    public void updateRefundStatus(Long refundId, RefundStatus status, Integer refundAmount) {
        log.info("환불 상태 변경 : 환불 ID: {}, 상태: {}, 환불 금액: {}", refundId, status, refundAmount);
        RefundDTO refund = getRefundDetail(refundId);

        // 환불 금액 업데이트 (필요 시)
        if (refundAmount != null && refundAmount > 0) {
            refundMapper.updateRefundAmount(refundId, refundAmount);
            refund.setRefundAmount(refundAmount);
        }

        // 환불 완료 처리
        if (status == RefundStatus.COMPLETED) {
            int remainingAmount = (refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount()) - refund.getRefundAmount();
            updateRemainingRefundAmount(refundId, remainingAmount);
            if (refund.getOrderDetailId() != null) {
                updateOrderDetailRefundStatus(refund.getOrderDetailId(), 3);
            }
            logTransaction(refund);
            sendNotification(refund);
        }

        refundMapper.updateRefundStatus(refundId, status.getCode());
        log.info("환불 상태 변경됨 - 환불 ID: {}, 상태: {}", refundId, status);
    }

    // 환불 후 남은 금액 업데이트
    @Override
    public void updateRemainingRefundAmount(Long refundId, int remainingAmount) {
        if (remainingAmount < 0) {
            throw new IllegalStateException("남은 환불 금액이 0원보다 아래임");
        }
        refundMapper.updateRemainingRefundAmount(refundId, remainingAmount);
        log.info("남은 환불 가능 금액 업데이트 완료 : 환불 ID: {}, 남은 금액: {}", refundId, remainingAmount);
    }

    // 주문 상세 내역의 환불 상태 변경
    @Override
    public void updateOrderDetailRefundStatus(Long orderDetailId, int status) {
        refundMapper.updateOrderDetailRefundStatus(orderDetailId, status);
        log.info("주문 상세 환불 상태 변경 : 주문 상세 ID: {}, 새로운 상태: {}", orderDetailId, status);
    }

    // 환불 트랜잭션 로그 기록
    @Override
    public void logTransaction(RefundDTO refund) {
        refundMapper.insertSalesTransactionLog(
                refund.getOrderId(),
                refund.getOrderDetailId(),
                null,
                refund.getUserId(),
                refund.getRefundAmount(),
                2
        );
        log.info("환불 트랜잭션 기록 완료 : 환불 ID: {}", refund.getRefundId());
    }

    // 사용자에게 환불 알림 전송
    @Override
    public void sendNotification(RefundDTO refund) {
        String message = refund.getOrderDetailId() != null ? "부분 환불이 완료되었습니다" : "환불이 완료되었습니다";
        refundMapper.insertNotification(
                refund.getUserId(),
                1,
                message,
                refund.getRefundId()
        );
        log.info("환불 알림 전송 완료 :  환불 ID: {}", refund.getRefundId());
    }

    // 환불 건수 조회
    @Override
    public int getRefundCount(Integer status, String orderId) {
        log.info("환불 건수 조회 :  상태: {}, 주문 ID: {}", status, orderId);
        return refundMapper.getRefundCount(status, orderId);
    }
}