package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/sales")
@RequiredArgsConstructor
public class AdminSalesController {

    private final AdminSalesService adminSalesService;

    @GetMapping
    public String getSalesDashboard(Model model) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // 최근 7일
        List<String> monthlyLabels = adminSalesService.getDailyLabels(startDate, endDate);
        List<Integer> monthlySalesAmount = adminSalesService.getDailySalesAmount(startDate, endDate);
        List<Integer> monthlySalesQuantity = adminSalesService.getDailySalesQuantity(startDate, endDate);
        model.addAttribute("monthlyLabels", monthlyLabels);
        model.addAttribute("monthlySalesAmount", monthlySalesAmount);
        model.addAttribute("monthlySalesQuantity", monthlySalesQuantity);
        return "admin/SalesStatus";
    }
}