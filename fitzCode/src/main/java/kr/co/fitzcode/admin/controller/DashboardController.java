package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String showAdminDashboard(Model model) {
        // 1대1 문의 상태별 개수 데이터
        Map<String, Integer> inquiryStatusCounts = dashboardService.getInquiryStatusCounts();
        model.addAttribute("inquiryStatusCounts", inquiryStatusCounts);

        // 주간 신규 가입자 수 데이터
        Map<LocalDate, Integer> weeklyNewUsers = dashboardService.getWeeklyNewUsers();
        List<String> newUserDateLabels = weeklyNewUsers.keySet().stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("M/d")))
                .collect(Collectors.toList());
        List<Integer> newUserCounts = new ArrayList<>(weeklyNewUsers.values());
        model.addAttribute("weeklyNewUserDates", newUserDateLabels);
        model.addAttribute("weeklyNewUserCounts", newUserCounts);

        // 주간 방문자 수 데이터
        Map<LocalDate, Integer> weeklyVisitors = dashboardService.getWeeklyVisitors();
        List<String> visitorDateLabels = weeklyVisitors.keySet().stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("M/d")))
                .collect(Collectors.toList());
        List<Integer> visitorCounts = new ArrayList<>(weeklyVisitors.values());
        model.addAttribute("weeklyVisitorDates", visitorDateLabels);
        model.addAttribute("weeklyVisitorCounts", visitorCounts);

        // KPI 데이터
        model.addAttribute("todayOrdersCount", dashboardService.getTodayOrdersCount());
        model.addAttribute("todayTotalSales", dashboardService.getTodayTotalSales());
        model.addAttribute("cancelReturnsCount", dashboardService.getCancelReturnsCount());
        model.addAttribute("currentVisitors", dashboardService.getCurrentVisitors());

        // 등급별 회원 수 데이터
        Map<Integer, Integer> memberTierCounts = dashboardService.getMemberTierCounts();
        model.addAttribute("memberTierCounts", memberTierCounts);
        return "admin/dashboard/dashboard";
    }
}