package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminRefundService;
import kr.co.fitzcode.common.dto.RefundDTO;
import kr.co.fitzcode.common.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/products/refund")
@RequiredArgsConstructor
public class AdminRefundController {

    private final AdminRefundService refundService;

    @GetMapping
    public String getRefundList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "orderId", required = false) String orderId,
            Model model) {
        log.info("Fetching refund list - page: {}, size: {}, status: {}, orderId: {}", page, size, status, orderId);
        List<RefundDTO> refunds = refundService.getRefundList(page, size, status, orderId);
        int totalCount = refundService.getRefundCount(status, orderId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        model.addAttribute("refunds", refunds);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("status", status);
        model.addAttribute("orderId", orderId);
        log.info("User authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return "admin/refund/refundList";
    }

    @GetMapping("/{refundId}")
    public String getRefundDetail(@PathVariable Long refundId, Model model) {
        log.info("Fetching refund detail for refundId: {}", refundId);
        log.info("User authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        RefundDTO refund = refundService.getRefundDetail(refundId);
        if (refund == null) {
            model.addAttribute("errorMessage", "환불 기록을 찾을 수 없습니다. 관리자 확인 필요.");
            return "admin/refund/refundList";
        }
        model.addAttribute("refund", refund);
        return "admin/refund/refundDetail";
    }

    @PostMapping("/{refundId}/updateStatus")
    public String updateRefundStatus(
            @PathVariable Long refundId,
            @RequestParam("status") Integer status,
            @RequestParam(value = "customRefundAmount", required = false) Integer customRefundAmount,
            Model model) {
        log.info("Updating refund status for refundId: {}, new status: {}", refundId, status);
        try {
            RefundDTO refund = refundService.getRefundDetail(refundId);
            if (customRefundAmount != null && customRefundAmount > 0 && customRefundAmount <= (refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount())) {
                refund.setRefundAmount(customRefundAmount);
            }
            refundService.updateRefundStatus(refundId, RefundStatus.fromCode(status));
            return "redirect:/admin/products/refund";
        } catch (Exception e) {
            log.error("Failed to update refund status for refundId: {}, error: {}", refundId, e.getMessage());
            model.addAttribute("errorMessage", "상태 변경에 실패했습니다: " + e.getMessage());
            return "admin/refund/refundDetail";
        }
    }

    @PostMapping("/{refundId}/updateAmount")
    @ResponseBody
    public Map<String, Object> updateRefundAmount(@PathVariable Long refundId, @RequestBody RefundDTO request) {
        log.info("Updating refund amount for refundId: {}", refundId);
        try {
            RefundDTO refund = refundService.getRefundDetail(refundId);
            if (request.getRefundAmount() != null && request.getRefundAmount() > 0 && request.getRefundAmount() <= (refund.getRemainingRefundAmount() != null ? refund.getRemainingRefundAmount() : refund.getPaymentAmount())) {
                refund.setRefundAmount(request.getRefundAmount());
                // 여기에 DB 업데이트 로직 추가 (현재는 DTO만 수정)
                return Map.of("success", true);
            } else {
                return Map.of("success", false, "message", "유효한 부분 환불 금액을 입력하세요.");
            }
        } catch (Exception e) {
            log.error("Failed to update refund amount for refundId: {}, error: {}", refundId, e.getMessage());
            return Map.of("success", false, "message", "오류 발생: " + e.getMessage());
        }
    }
}