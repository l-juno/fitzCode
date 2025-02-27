package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import kr.co.fitzcode.admin.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    /**
     * 문의 목록 조회
     * 기본적으로 1페이지, 10개씩 조회 (페이징 가능)
     * @param page 현재 페이지 (기본값: 1)
     * @param size 페이지당 표시할 문의 개수 (기본값: 10)
     * @param model 조회된 문의 목록을 담을 모델
     * @return 문의 목록 페이지 (admin/inquiry/inquiryList)
     */
    @GetMapping
    public String getInquiryList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        List<InquiryDTO> inquiries = inquiryService.getInquiryList(page, size);
        model.addAttribute("inquiries", inquiries);
        return "admin/inquiry/inquiryList";
    }

    /**
     * 특정 문의 상세 조회
     * @param id 조회할 문의 ID
     * @param model 조회된 문의 정보를 담을 모델
     * @return 문의 상세 페이지 (admin/inquiry/inquiryDetail)
     */
    @GetMapping("/{id}")
    public String getInquiryDetail(@PathVariable int id, Model model) {
        InquiryDTO inquiry = inquiryService.getInquiryDetail(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry/inquiryDetail";
    }

    /**
     * 문의 상태 변경 (ex: 대기 → 답변완료)
     * @param id 변경할 문의 ID
     * @param status 변경할 상태 코드 (1: 대기, 2: 답변완료, 3: 종료)
     * @return 문의 상세 페이지로 리다이렉트
     */
    @PostMapping("/{id}/status")
    public String updateInquiryStatus(@PathVariable int id, @RequestParam int status) {
        inquiryService.updateInquiryStatus(id, status);
        return "redirect:/admin/inquiries/" + id;
    }

    /**
     * 문의 카테고리 변경 (ex: 배송 관련 → 환불 관련)
     * @param id 변경할 문의 ID
     * @param category 변경할 카테고리 코드
     * @return 문의 상세 페이지로 리다이렉트
     */
    @PostMapping("/{id}/category")
    public String updateInquiryCategory(@PathVariable int id, @RequestParam int category) {
        inquiryService.updateInquiryCategory(id, category);
        return "redirect:/admin/inquiries/" + id;
    }

    /**
     * 문의 답변 등록 또는 수정
     * @param id 답변할 문의 ID
     * @param reply 답변 내용
     * @return 문의 상세 페이지로 리다이렉트
     */
    @PostMapping("/{id}/reply")
    public String updateInquiryReply(@PathVariable int id, @RequestParam String reply) {
        inquiryService.updateInquiryReply(id, reply);
        return "redirect:/admin/inquiries/" + id;
    }
}