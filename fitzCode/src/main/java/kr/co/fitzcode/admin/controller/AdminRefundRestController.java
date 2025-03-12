package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminRefundService;
import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/products/refund/api")
@RequiredArgsConstructor
public class AdminRefundRestController {

    private final AdminRefundService refundService;

    @Value("${portone.imp_key}")
    private String impKey;

    @Value("${portone.imp_secret}")
    private String impSecret;

    @PostMapping("/{refundId}/process")
    public ResponseEntity<Map<String, Object>> processRefund(
            @PathVariable Long refundId,
            @RequestParam(value = "customRefundAmount", required = false) Integer customRefundAmount,
            @RequestParam("status") Integer status) {

        Map<String, Object> response = new HashMap<>();
        RefundDTO refund = refundService.getRefundDetail(refundId);

        // 환불 가능한 금액 체크 (환불 거절에는 적용 안할거임)
        int effectiveRefundAmount = 0;
        if (status != RefundStatus.REJECTED.getCode()) {
            int remainingAmount = refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount();
            effectiveRefundAmount = (customRefundAmount != null && customRefundAmount > 0) ? customRefundAmount : refund.getCalculatedRefundAmount();
            if (effectiveRefundAmount <= 0 || effectiveRefundAmount > remainingAmount) {
                response.put("success", false);
                response.put("message", "유효한 환불 금액을 입력하세요 (최대: " + remainingAmount + "원)");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // 계좌 환불 시 계좌 정보
        if (refund.getRefundMethod() == 2 && status == RefundStatus.COMPLETED.getCode()) {
            if (refund.getAccountHolder() == null || refund.getBankName() == null || refund.getAccountNumber() == null) {
                response.put("success", false);
                response.put("message", "사용자의 계좌 정보가 등록되어 있지 않습니다. 계좌 등록 후 다시 시도해주세요.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        try {
            RefundStatus refundStatus = RefundStatus.fromCode(status);
            boolean refundSuccess = true;

            // 카드(1) 또는 간편 결제(3)일 경우 포트원 호출
            if ((refund.getRefundMethod() == 1 || refund.getRefundMethod() == 3) && refundStatus == RefundStatus.COMPLETED) {
                String accessToken = getAccessToken();
                refundSuccess = requestRefund(refund, effectiveRefundAmount, accessToken);
            } else if (refund.getRefundMethod() == 2 && refundStatus == RefundStatus.COMPLETED) {
                // 계좌 환불
                log.info("계좌 환불 처리: 환불 번호 {}, 금액 {}, 계좌 정보: {} {}",
                        refundId, effectiveRefundAmount, refund.getAccountHolder(), refund.getAccountNumber());
            }

            if (!refundSuccess) {
                response.put("success", false);
                response.put("message", "환불 요청 실패");
                return ResponseEntity.status(500).body(response);
            }

            // 환불 상태 업데이트
            refundService.updateRefundStatus(refundId, refundStatus, effectiveRefundAmount);

            response.put("success", true);
            response.put("message", refundStatus == RefundStatus.REJECTED ? "환불이 거절되었습니다" : "환불이 성공적으로 처리됨");
            response.put("refund", refundService.getRefundDetail(refundId));
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("환불 처리 중 오류 발생 refundId: {}, 오류 내용: {}", refundId, e.getMessage());
            response.put("success", false);
            response.put("message", "환불 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("imp_key", impKey);
        json.put("imp_secret", impSecret);

        log.info("포트원 액세스 토큰 요청 imp_key: {}, imp_secret: {}", impKey, impSecret);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://api.iamport.kr/users/getToken")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBodyString = response.body() != null ? response.body().string() : "Response Body 없음";
            log.info("포트원 응답 : HTTP {}, Response Body: {}", response.code(), responseBodyString);
            if (!response.isSuccessful()) {
                log.error("엑세스 토큰 요청 실패 : HTTP {}, 응답 본문: {}", response.code(), responseBodyString);
                throw new IOException("엑세스 토큰 요청 실패 : HTTP " + response.code() + " - " + responseBodyString);
            }
            JSONObject responseBody = new JSONObject(responseBodyString);
            if (responseBody.has("response")) {
                JSONObject responseData = responseBody.getJSONObject("response");
                return responseData.getString("access_token");
            } else {
                throw new IOException("Response 에서 액세스 토큰을 찾을 수 없음 : " + responseBodyString);
            }
        }
    }

    private boolean requestRefund(RefundDTO refund, int amount, String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("imp_uid", refund.getTransactionId());
        json.put("amount", amount);
        json.put("reason", refund.getRefundReason());
        json.put("checksum", refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount());

        log.info("환불 Request Body: {}", json.toString());

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://api.iamport.kr/payments/cancel")
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBodyString = response.body() != null ? response.body().string() : "Response Body 없음";
            log.info("포트원 응답2 : HTTP {}, Response Body: {}", response.code(), responseBodyString);
            if (!response.isSuccessful()) {
                log.error("환불 요청 실패 : HTTP {}, Response Body: {}", response.code(), responseBodyString);
                if (response.code() == 404) {
                    log.error("유효하지 않은 transactionId: {}", refund.getTransactionId());
                }
                return false;
            }
            JSONObject responseBody = new JSONObject(responseBodyString);
            return responseBody.optInt("code", -1) == 0;
        }
    }
}