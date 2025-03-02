package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import kr.co.fitzcode.admin.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

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
    @PostMapping("/{id}/reply")
    public String updateInquiryReply(@PathVariable int id, @RequestParam String reply) {
        inquiryService.updateInquiryReply(id, reply);
        return "redirect:/admin/inquiries/" + id;
    }
}