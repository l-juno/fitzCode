package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminInquiryService;
import kr.co.fitzcode.common.dto.InquiryDTO;
import kr.co.fitzcode.common.dto.NotificationDTO;
import kr.co.fitzcode.common.enums.NotificationType;
import kr.co.fitzcode.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
@Slf4j
public class AdminInquiryController {
    private final AdminInquiryService inquiryService;
    private final NotificationService notificationService;

    // 1:1 문의 목록 조회
    @GetMapping
    public String getInquiryList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 @RequestParam(required = false) List<Integer> category,
                                 @RequestParam(required = false) List<Integer> status,
                                 Model model) {
        int totalCount = inquiryService.getTotalInquiryCount(category, status);
        int totalPages = inquiryService.calculateTotalPages(totalCount, size);
        List<InquiryDTO> inquiries = inquiryService.getInquiryList(page, size, category, status);

        model.addAttribute("inquiries", inquiries);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("category", category);
        model.addAttribute("status", status);

        return "admin/inquiry/inquiryList";
    }

    // 특정 문의의 상세 정보를 조회
    @GetMapping("/{id}")
    public String getInquiryDetail(@PathVariable int id, Model model) {
        InquiryDTO inquiry = inquiryService.getInquiryDetail(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry/inquiryDetail";
    }

    // 문의 상태 업데이트
    @PostMapping("/{id}/status")
    public String updateInquiryStatus(@PathVariable int id, @RequestParam int status) {
        inquiryService.updateInquiryStatus(id, status);
        return "redirect:/admin/inquiries/" + id;
    }

    // 문의 카테고리 업데이트
    @PostMapping("/{id}/category")
    public String updateInquiryCategory(@PathVariable int id, @RequestParam int category) {
        inquiryService.updateInquiryCategory(id, category);
        return "redirect:/admin/inquiries/" + id;
    }

    // 문의에 답변
    @Transactional
    @PostMapping("/{id}/reply")
    public String updateInquiryReply(@PathVariable int id, @RequestParam String reply, Principal principal, Model model) {
        try {
            log.info("문의 답변 시작 - 문의 ID: {}, 답변: {}", id, reply);
            InquiryDTO inquiry = inquiryService.getInquiryDetail(id);
            inquiryService.updateInquiryReply(id, reply);
            log.info("문의 답변 저장 완료 - 문의 ID: {}", id);

            // 알림 생성 및 SSE 이벤트 발송
            log.info("알림 생성 시작 - 사용자 ID: {}, 문의 ID: {}", inquiry.getUserId(), inquiry.getInquiryId());
            notificationService.sendNotificationToUser("INQUIRY_ANSWERED", inquiry, inquiry.getUserId());
            log.info("알림 생성 및 전송 완료");

            return "redirect:/admin/inquiries/" + id;
        } catch (Exception e) {
            log.error("1대1 문의 답변 처리 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "404-error";
        }
    }
}