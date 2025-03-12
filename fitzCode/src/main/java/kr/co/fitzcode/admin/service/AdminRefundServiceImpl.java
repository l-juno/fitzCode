package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminRefundMapper;
import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRefundServiceImpl implements AdminRefundService {

    private final AdminRefundMapper refundMapper;
    private static final int PAGE_SIZE = 10;

    @Override
    public List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId) {
        if (page < 1) page = 1;
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        int offset = (page - 1) * size;
        log.info("환불 목록 조회 : 페이지: {}, 개수: {}, 상태: {}, 주문 ID: {}", page, size, status, orderId);
        List<RefundDTO> refunds = refundMapper.getRefundList(size, offset, status, orderId);
        log.debug("Fetched refunds size: {}", refunds.size());

        for (RefundDTO refund : refunds) {
            List<RefundDTO.OrderDetailDTO> items = refundMapper.getRequestedRefundItems(refund.getOrderId());
            refund.setRequestedItems(items);
            log.debug("Refund ID: {}, Requested items size: {}, Requested refund amount: {}",
                    refund.getRefundId(), items != null ? items.size() : 0, refund.getRequestedRefundAmount());
        }
        return refunds;
    }

    @Override
    public RefundDTO getRefundDetail(Long refundId) {
        log.info("환불 상세 조회 - 환불 ID: {}", refundId);
        RefundDTO refund = refundMapper.getRefundDetail(refundId);
        if (refund == null) {
            log.warn("환불 기록 없음 - 환불 ID: {}", refundId);
            throw new RuntimeException("환불 기록을 찾을 수 없음");
        }
        log.debug("Fetched RefundDTO: refundId={}, orderId={}, paymentAmount={}, refundAmount={}, remainingRefundAmount={}",
                refund.getRefundId(), refund.getOrderId(), refund.getPaymentAmount(), refund.getRefundAmount(), refund.getRemainingRefundAmount());
        List<RefundDTO.OrderDetailDTO> items = refundMapper.getRequestedRefundItems(refund.getOrderId());
        refund.setRequestedItems(items);
        log.debug("Requested items size: {}, items: {}", items != null ? items.size() : 0, items);
        int calculatedAmount = refund.getCalculatedRefundAmount();
        log.debug("Calculated amount after setting items: {}", calculatedAmount);
        if (refund.getRefundAmount() == null || refund.getRefundAmount() == 0) {
            int adjustedAmount = Math.min(calculatedAmount, refund.getPaymentAmount() != null ? refund.getPaymentAmount() : calculatedAmount);
            refund.setRefundAmount(adjustedAmount);
            log.debug("Adjusted refund amount: {}", adjustedAmount);
        } else {
            // db에서 가져온 refundAmount를 유지
            log.debug("Using existing refund amount from DB: {}", refund.getRefundAmount());
        }
        // remaining_refund_amount를 db값으로 유지
        int remainingAmount = refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount()
                : (refund.getPaymentAmount() - (refund.getRefundAmount() != null ? refund.getRefundAmount() : 0));
        if (remainingAmount < 0) {
            log.warn("Remaining refund amount is negative, resetting to 0. refundId: {}", refundId);
            remainingAmount = 0;
        }
        refundMapper.updateRemainingRefundAmount(refundId, remainingAmount);
        refund.setRemainingRefundAmount(remainingAmount);
        log.debug("Updated remaining refund amount: {}", remainingAmount);
        return refund;
    }

    @Override
    @Transactional
    public void updateRefundStatus(Long refundId, RefundStatus status, Integer refundAmount) {
        log.info("환불 상태 변경 : 환불 ID: {}, 상태: {}, 환불 금액: {}", refundId, status, refundAmount);
        RefundDTO refund = getRefundDetail(refundId);

        int effectiveRefundAmount = (refundAmount != null && refundAmount > 0) ? refundAmount : refund.getCalculatedRefundAmount();
        if (status != RefundStatus.REJECTED) {
            int remainingAmount = refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : 0;
            if (effectiveRefundAmount > remainingAmount) {
                throw new IllegalArgumentException("유효한 환불 금액을 입력하세요 (최대: " + remainingAmount + "원)");
            }
            // 누적 환불 금액 업데이트
            int newRefundAmount = (refund.getRefundAmount() != null ? refund.getRefundAmount() : 0) + effectiveRefundAmount;
            refundMapper.updateRefundAmount(refundId, newRefundAmount);
            refund.setRefundAmount(newRefundAmount);

            // remaining_refund_amount 업데이트
            int newRemainingAmount = remainingAmount - effectiveRefundAmount;
            if (newRemainingAmount < 0) {
                throw new IllegalStateException("남은 환불 금액이 0원보다 아래임");
            }
            refundMapper.updateRemainingRefundAmount(refundId, newRemainingAmount);
            refund.setRemainingRefundAmount(newRemainingAmount);
            log.debug("Updated refund amount: {}, remaining refund amount: {}", newRefundAmount, newRemainingAmount);
        }

        if (status == RefundStatus.COMPLETED) {
            updateRequestedItemsStatus(refund, RefundStatus.COMPLETED.getCode());
            logTransaction(refund);
            sendNotification(refund, refund.getRequestedItems() != null && !refund.getRequestedItems().isEmpty() ? "부분 환불이 완료되었습니다" : "환불이 완료되었습니다");
        } else if (status == RefundStatus.REJECTED) {
            updateRequestedItemsStatus(refund, RefundStatus.REJECTED.getCode());
            sendNotification(refund, "환불이 거절되었습니다");
        } else if (status == RefundStatus.PROCESSING) {
            updateRequestedItemsStatus(refund, RefundStatus.PROCESSING.getCode());
        }

        refundMapper.updateRefundStatus(refundId, status.getCode());
        log.info("환불 상태 변경됨 - 환불 ID: {}, 상태: {}", refundId, status);
    }

    @Override
    @Transactional
    public void createRefund(Long orderId, String refundReason, Integer refundMethod) {
        log.info("환불 신청 생성 - orderId: {}, reason: {}, method: {}", orderId, refundReason, refundMethod);
        // 기존 환불 기록 확인
        RefundDTO existingRefund = refundMapper.getRefundDetailByOrderId(orderId);
        if (existingRefund != null) {
            throw new IllegalStateException("이미 환불 신청이 존재합니다: refund_id = " + existingRefund.getRefundId());
        }

        // 새로운 환불 DTO 생성
        RefundDTO refund = new RefundDTO();
        refund.setOrderId(orderId);
        refund.setRefundReason(refundReason);
        refund.setRefundMethod(refundMethod);
        refund.setRequestedAt(LocalDateTime.now());
        refund.setRefundStatusCode(RefundStatus.REQUESTED.getCode());
        refund.setRefundStatus(RefundStatus.REQUESTED);

        // requestedItems 조회
        List<RefundDTO.OrderDetailDTO> items = refundMapper.getRequestedRefundItems(orderId);
        refund.setRequestedItems(items);

        // refund_amount를 요청된 상품의 총 구매 가격으로 설정
        int requestedRefundAmount = refund.getRequestedRefundAmount();
        refund.setRefundAmount(requestedRefundAmount);
        log.debug("Initial refund amount set to: {}", requestedRefundAmount);

        // paymentAmount 조회
        Integer paymentAmount = refundMapper.getPaymentAmountByOrderId(orderId);
        refund.setPaymentAmount(paymentAmount != null ? paymentAmount : 0);
        log.debug("Payment amount for orderId {}: {}", orderId, paymentAmount);

        // remaining_refund_amount 초기화 (결제 금액 - 환불 금액)
        int initialRemainingAmount = paymentAmount != null ? paymentAmount - requestedRefundAmount : 0;
        if (initialRemainingAmount < 0) {
            throw new IllegalStateException("결제 금액이 요청 환불 금액보다 작습니다: paymentAmount=" + paymentAmount + ", requestedRefundAmount=" + requestedRefundAmount);
        }
        refund.setRemainingRefundAmount(initialRemainingAmount);
        log.debug("Initial remaining refund amount: {}", initialRemainingAmount);

        // REFUND 테이블에 삽입
        refundMapper.insertRefund(refund);
        log.info("환불 신청 생성 완료 - refund_id: {}", refund.getRefundId());

        // ORDER_DETAIL 상태 업데이트
        updateRequestedItemsStatus(refund, RefundStatus.REQUESTED.getCode());
    }

    private void updateRequestedItemsStatus(RefundDTO refund, int status) {
        if (refund.getRequestedItems() != null) {
            refund.getRequestedItems().forEach(item ->
                    refundMapper.updateOrderDetailRefundStatus(item.getOrderDetailId(), status)
            );
        }
    }

    @Override
    public void updateRemainingRefundAmount(Long refundId, int remainingAmount) {
        if (remainingAmount < 0) {
            throw new IllegalStateException("남은 환불 금액이 0원보다 아래임");
        }
        refundMapper.updateRemainingRefundAmount(refundId, remainingAmount);
        log.info("남은 환불 가능 금액 업데이트 완료 : 환불 ID: {}, 남은 금액: {}", refundId, remainingAmount);
    }

    @Override
    public void updateOrderDetailRefundStatus(Long orderDetailId, int status) {
        refundMapper.updateOrderDetailRefundStatus(orderDetailId, status);
        log.info("주문 상세 환불 상태 변경 : 주문 상세 ID: {}, 새로운 상태: {}", orderDetailId, status);
    }

    @Override
    public void logTransaction(RefundDTO refund) {
        refundMapper.insertSalesTransactionLog(
                refund.getOrderId(),
                refund.getOrderDetailId(),
                refund.getUserId(),
                refund.getRefundAmount() != null ? refund.getRefundAmount().longValue() : null,
                2
        );
        log.info("환불 트랜잭션 기록 완료 : 환불 ID: {}", refund.getRefundId());
    }

    @Override
    public void sendNotification(RefundDTO refund, String message) {
        refundMapper.insertNotification(
                refund.getUserId(),
                1,
                message,
                refund.getRefundId()
        );
        log.info("환불 알림 전송 완료 : 환불 ID: {}", refund.getRefundId());
    }

    @Override
    public int getRefundCount(Integer status, String orderId) {
        log.info("환불 건수 조회 : 상태: {}, 주문 ID: {}", status, orderId);
        return refundMapper.getRefundCount(status, orderId);
    }
}