package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminRefundMapper;
import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRefundServiceImpl implements AdminRefundService {

    private final AdminRefundMapper refundMapper;

    @Value("${portone.api.key}")
    private String apiKey;

    @Value("${portone.api.secret}")
    private String apiSecret;

    private static final int PAGE_SIZE = 10;
    private static final String PORTONE_TOKEN_URL = "https://api.portone.io/v2/auth/token";
    private static final String PORTONE_CANCEL_URL = "https://api.portone.io/payments/%s/cancel";

    @Override
    public List<RefundDTO> getRefundList(int page, int size, Integer status, String orderId) {
        if (page < 1) page = 1;
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        int offset = (page - 1) * size;
        log.info("Fetching refund list - page: {}, size: {}, status: {}, orderId: {}", page, size, status, orderId);
        return refundMapper.getRefundList(size, offset, status, orderId);
    }

    @Override
    public RefundDTO getRefundDetail(Long refundId) {
        log.info("Fetching refund detail for refundId: {}", refundId);
        RefundDTO refund = refundMapper.getRefundDetail(refundId);
        if (refund == null) {
            log.warn("No refund record found for refundId: {}. Admin action aborted.", refundId);
            throw new RuntimeException("환불 기록을 찾을 수 없습니다. 관리자 확인 필요.");
        }
        return refund;
    }

    @Override
    @Transactional
    public void updateRefundStatus(Long refundId, RefundStatus status) {
        log.info("Admin action: Updating refund status for refundId: {}, new status: {}", refundId, status);
        RefundDTO refund = getRefundDetail(refundId);

        if (status == RefundStatus.COMPLETED) {
            if (refund.getRefundMethod() == 1) {
                processCardRefund(refund);
            } else if (refund.getRefundMethod() == 2) {
                processTransferRefund(refund);
            } else {
                throw new UnsupportedOperationException("지원되지 않는 환불 방법: " + refund.getRefundMethod() + ". 관리자 확인 필요.");
            }
            updateRemainingRefundAmount(refundId, refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() - refund.getRefundAmount() : 0);
            if (refund.getOrderDetailId() != null) {
                updateOrderDetailRefundStatus(refund.getOrderDetailId(), 3);
            }
            logTransaction(refund);
            sendNotification(refund);
        }

        refundMapper.updateRefundStatus(refundId, status.getCode());
        log.info("Admin action completed: Refund status updated to {} for refundId: {}.", status, refundId);
    }

    @Override
    public void updateRemainingRefundAmount(Long refundId, int remainingAmount) {
        if (remainingAmount < 0) {
            throw new IllegalStateException("남은 환불 가능 금액이 음수가 될 수 없습니다. 관리자 확인 필요.");
        }
        refundMapper.updateRemainingRefundAmount(refundId, remainingAmount);
        log.info("Updated remaining refund amount for refundId: {} to {}.", refundId, remainingAmount);
    }

    @Override
    public void updateOrderDetailRefundStatus(Long orderDetailId, int status) {
        refundMapper.updateOrderDetailRefundStatus(orderDetailId, status);
        log.info("Updated refund status to {} for orderDetailId: {}.", status, orderDetailId);
    }

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
        log.info("Logged refund transaction for refundId: {}.", refund.getRefundId());
    }

    @Override
    public void sendNotification(RefundDTO refund) {
        String message = refund.getOrderDetailId() != null ? "특정 상품에 대한 부분 환불이 완료되었습니다." : "환불이 완료되었습니다.";
        refundMapper.insertNotification(
                refund.getUserId(),
                1,
                message,
                refund.getRefundId()
        );
        log.info("Sent notification for refundId: {}.", refund.getRefundId());
    }

    @Override
    public int getRefundCount(Integer status, String orderId) {
        log.info("Fetching refund count - status: {}, orderId: {}", status, orderId);
        return refundMapper.getRefundCount(status, orderId);
    }

    private void processCardRefund(RefundDTO refund) {
        if (refund.getTransactionId() == null || refund.getTransactionId().isEmpty()) {
            throw new IllegalStateException("transaction_id가 없습니다. 결제 구현자에게 확인하세요.");
        }
        if (refund.getRefundAmount() <= 0) {
            throw new IllegalArgumentException("환불 금액은 0보다 커야 합니다. 관리자 확인 필요.");
        }
        if (refund.getRemainingRefundAmount() != null && refund.getRefundAmount() > refund.getRemainingRefundAmount()) {
            throw new IllegalStateException("요청된 환불 금액이 남은 환불 가능 금액을 초과했습니다. 관리자 확인 필요.");
        }

        try {
            String accessToken = getPortOneAccessToken();
            boolean success = callPortOneCancel(refund, accessToken);
            if (!success) {
                throw new RuntimeException("포트원 카드 결제 취소 실패. 관리자 확인 및 재시도 필요.");
            }
            log.info("Admin action succeeded: Card refund processed for transactionId: {}, amount: {}.", refund.getTransactionId(), refund.getRefundAmount());
        } catch (Exception e) {
            log.error("Admin action failed: Failed to process card refund for transactionId: {}, error: {}.", refund.getTransactionId(), e.getMessage());
            throw new RuntimeException("환불 처리 중 오류 발생: " + e.getMessage() + ". 관리자에게 문의하세요.");
        }
    }

    private void processTransferRefund(RefundDTO refund) {
        log.info("Admin action initiated: Processing transfer refund for refundId: {}, amount: {}.", refund.getRefundId(), refund.getRefundAmount());
        // TODO: refundAccount 계좌 이체 환불 처리
    }

    private String getPortOneAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("imp_key", apiKey);
        json.put("imp_secret", apiSecret);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(PORTONE_TOKEN_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get PortOne access token: " + response + ". 관리자 확인 필요.");
            }
            JSONObject responseBody = new JSONObject(response.body().string());
            return responseBody.getJSONObject("response").getString("access_token");
        }
    }

    private boolean callPortOneCancel(RefundDTO refund, String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("amount", refund.getRefundAmount());
        json.put("reason", refund.getRefundReason());

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        String url = String.format(PORTONE_CANCEL_URL, refund.getTransactionId());
        log.info("Admin action: Sending cancel request to PortOne - URL: {}, Body: {}", url, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                log.error("Admin action failed: PortOne cancel request failed: {}", errorBody);
                handleCancelError(response, errorBody);
                return false;
            }
            JSONObject responseBody = new JSONObject(response.body().string());
            int code = responseBody.getInt("code");
            if (code == 0) {
                log.info("Admin action succeeded: PortOne cancel succeeded: {}", responseBody.toString());
                return true;
            } else {
                log.warn("Admin action failed: PortOne cancel failed with code: {}, message: {}", code, responseBody.getString("message"));
                handleCancelError(response, responseBody.toString());
                return false;
            }
        }
    }

    private void handleCancelError(Response response, String errorBody) throws IOException {
        JSONObject error = new JSONObject(errorBody);
        String type = error.getString("type");
        String message = error.getString("message");
        switch (type) {
            case "INVALID_REQUEST":
                throw new IllegalArgumentException("잘못된 요청: " + message + ". 관리자 확인 및 재시도 필요.");
            case "NEGATIVE_PROMOTION_ADJUSTED_CANCEL_AMOUNT":
                throw new IllegalStateException("프로모션 조정 취소 금액 음수: " + message + ". 관리자 확인 필요.");
            case "PROMOTION_DISCOUNT_RETAIN_OPTION_SHOULD_NOT_BE_CHANGED":
                throw new IllegalStateException("프로모션 혜택 유지 옵션 변경 불가: " + message + ". 관리자 확인 필요.");
            case "UNAUTHORIZED":
                throw new SecurityException("인증 오류: " + message + ". 관리자 설정 확인 필요.");
            case "FORBIDDEN":
                throw new SecurityException("접근 거부: " + message + ". 관리자 권한 확인 필요.");
            case "PAYMENT_NOT_FOUND":
                throw new RuntimeException("결제 건을 찾을 수 없음: " + message + ". 관리자 확인 필요.");
            case "CANCELLABLE_AMOUNT_CONSISTENCY_BROKEN":
                throw new IllegalStateException("취소 가능 잔액 검증 실패: " + message + ". 관리자 확인 필요.");
            case "CANCEL_AMOUNT_EXCEEDS_CANCELLABLE_AMOUNT":
                throw new IllegalStateException("취소 금액 초과: " + message + ". 관리자 확인 필요.");
            case "CANCEL_TAX_AMOUNT_EXCEEDS_CANCELLABLE_TAX_AMOUNT":
                throw new IllegalStateException("취소 과세 금액 초과: " + message + ". 관리자 확인 필요.");
            case "CANCEL_TAX_FREE_AMOUNT_EXCEEDS_CANCELLABLE_TAX_FREE_AMOUNT":
                throw new IllegalStateException("취소 면세 금액 초과: " + message + ". 관리자 확인 필요.");
            case "PAYMENT_ALREADY_CANCELLED":
                throw new IllegalStateException("결제가 이미 취소됨: " + message + ". 관리자 확인 필요.");
            case "PAYMENT_NOT_PAID":
                throw new IllegalStateException("결제가 완료되지 않음: " + message + ". 관리자 확인 필요.");
            case "SUM_OF_PARTS_EXCEEDS_CANCEL_AMOUNT":
                throw new IllegalStateException("하위 항목 합계 초과: " + message + ". 관리자 확인 필요.");
            case "PG_PROVIDER":
                throw new RuntimeException("PG사 오류: " + message + ". 관리자 문의 필요.");
            default:
                throw new RuntimeException("알 수 없는 오류: " + message + ". 관리자 문의 필요.");
        }
    }
}