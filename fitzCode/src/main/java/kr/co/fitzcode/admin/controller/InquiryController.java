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

    @GetMapping
    public String getInquiryList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 @RequestParam(required = false) List<Integer> category,
                                 @RequestParam(required = false) List<Integer> status,
                                 Model model) {
        int totalCount = inquiryService.getTotalInquiryCount(category, status);  // 필터링된 총 개수 조회
        int totalPages = inquiryService.calculateTotalPages(totalCount, size);
        List<InquiryDTO> inquiries = inquiryService.getInquiryList(page, size, category, status);  // 필터링된 데이터 조회

        model.addAttribute("inquiries", inquiries);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("category", category);
        model.addAttribute("status", status);

        return "admin/inquiry/inquiryList";
    }

    @GetMapping("/{id}")
    public String getInquiryDetail(@PathVariable int id, Model model) {
        InquiryDTO inquiry = inquiryService.getInquiryDetail(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry/inquiryDetail";
    }

    @PostMapping("/{id}/status")
    public String updateInquiryStatus(@PathVariable int id, @RequestParam int status) {
        inquiryService.updateInquiryStatus(id, status);
        return "redirect:/admin/inquiries/" + id;
    }

    @PostMapping("/{id}/category")
    public String updateInquiryCategory(@PathVariable int id, @RequestParam int category) {
        inquiryService.updateInquiryCategory(id, category);
        return "redirect:/admin/inquiries/" + id;
    }

    @PostMapping("/{id}/reply")
    public String updateInquiryReply(@PathVariable int id, @RequestParam String reply) {
        inquiryService.updateInquiryReply(id, reply);
        return "redirect:/admin/inquiries/" + id;
    }
}