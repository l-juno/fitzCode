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

    // 환불 처리 요청
    @PostMapping("/{refundId}/process")
    public ResponseEntity<Map<String, Object>> processRefund(
            @PathVariable Long refundId,
            @RequestParam("customRefundAmount") Integer customRefundAmount,
            @RequestParam("status") Integer status,
            @RequestParam(value = "refundHolder", required = false) String refundHolder,
            @RequestParam(value = "refundBank", required = false) String refundBank,
            @RequestParam(value = "refundAccount", required = false) String refundAccount,
            @RequestParam(value = "refundTel", required = false) String refundTel) {

        Map<String, Object> response = new HashMap<>();
        RefundDTO refund = refundService.getRefundDetail(refundId);

        // 환불 가능한 금액 체크
        int remainingAmount = refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount();
        if (customRefundAmount <= 0 || customRefundAmount > remainingAmount) {
            response.put("success", false);
            response.put("message", "유효한 환불 금액을 입력하세요 (최대: " + remainingAmount + "원)");
            return ResponseEntity.badRequest().body(response);
        }

        // 가상계좌 환불 시 계좌 정보 확인
        if (refund.getRefundMethod() == 2 && (refundHolder == null || refundBank == null || refundAccount == null)) {
            response.put("success", false);
            response.put("message", "계좌 정보를 모두 입력하세요");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 포트원 액세스 토큰 발급
            String accessToken = getAccessToken();

            // 환불 요청 실행
            boolean refundSuccess = requestRefund(refund, customRefundAmount, accessToken, refundHolder, refundBank, refundAccount, refundTel);
            if (!refundSuccess) {
                response.put("success", false);
                response.put("message", "환불 요청 실패");
                return ResponseEntity.status(500).body(response);
            }

            // 환불 상태 업데이트
            refundService.updateRefundStatus(refundId, RefundStatus.fromCode(status), customRefundAmount);

            response.put("success", true);
            response.put("message", "환불이 성공적으로 처리됨");
            response.put("refund", refundService.getRefundDetail(refundId));
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("환불 처리 중 오류 발생 refundId: {}, 오류 내용: {}", refundId, e.getMessage());
            response.put("success", false);
            response.put("message", "환불 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 포트원 액세스 토큰 발급
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

    // 포트원에 환불 요청
    private boolean requestRefund(RefundDTO refund, int amount, String accessToken,
                                  String refundHolder, String refundBank, String refundAccount, String refundTel) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("imp_uid", refund.getTransactionId());
        json.put("amount", amount);
        json.put("reason", refund.getRefundReason());
        json.put("checksum", refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount());

        if (refund.getRefundMethod() == 2) {
            json.put("refund_holder", refundHolder);
            json.put("refund_bank", refundBank);
            json.put("refund_account", refundAccount);
            if (refundTel != null) json.put("refund_tel", refundTel);
        }

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