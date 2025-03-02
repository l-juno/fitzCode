package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String showAdminDashboard(Model model) {
        // 1대1 문의 상태별 개수 데이터 추가
        Map<String, Integer> inquiryStatusCounts = dashboardService.getInquiryStatusCounts();
        model.addAttribute("inquiryStatusCounts", inquiryStatusCounts);

        // 다른 대시보드 데이터 추가 (주간 신규 가입자, 등급별 회원 비율 등)


        return "admin/dashboard/dashboard"; // dashboard.html 템플릿 반환
    }
}