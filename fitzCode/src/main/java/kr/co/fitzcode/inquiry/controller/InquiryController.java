package kr.co.fitzcode.inquiry.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.inquiry.service.InquiryService;
import kr.co.fitzcode.user.dto.UserDto;
import kr.co.fitzcode.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @GetMapping("/inquiryForm")
    public void inquiryForm(Model model, HttpSession session) {
        session.setAttribute("userId", 2);
        int userId =  (int) session.getAttribute("userId");
        model.addAttribute("userId", userId);
    }

    @PostMapping("/inquiryForm")
    public String inquiryForm() {
        return null;
    }

}
