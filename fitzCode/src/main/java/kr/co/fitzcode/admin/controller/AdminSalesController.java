package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminSalesService;
import kr.co.fitzcode.common.dto.ChartDataDTO;
import kr.co.fitzcode.common.dto.SalesRankingDTO;
import kr.co.fitzcode.common.dto.SearchRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/sales")
@RequiredArgsConstructor
public class AdminSalesController {

    private final AdminSalesService adminSalesService;

    @GetMapping
    public String getSalesDashboard(Model model) {
        ChartDataDTO chartData = adminSalesService.getSalesDataForLast30Days();
        model.addAttribute("revenueData", chartData.getRevenueData());
        model.addAttribute("volumeData", chartData.getVolumeData());
        model.addAttribute("labels", adminSalesService.getDailyLabels());
        model.addAttribute("salesRanking", adminSalesService.getSalesRankingForLast14Days(1, 5)); // 첫 페이지 5개
        model.addAttribute("totalCount", adminSalesService.getTotalSalesRankingCount()); // 총 상품 수
        model.addAttribute("searchRanking", adminSalesService.getSearchRanking()); // 검색어 순위 추가
        model.addAttribute("searchTotalCount", adminSalesService.getTotalSearchRankingCount()); // 검색어 총 개수 추가
        return "/admin/sales/salesStatus";
    }

    @GetMapping("/ranking")
    @ResponseBody
    public List<SalesRankingDTO> getSalesRanking(@RequestParam("page") int page) {
        return adminSalesService.getSalesRankingForLast14Days(page, 5); // 페이지당 5개
    }

    @GetMapping("/total-count")
    @ResponseBody
    public int getTotalSalesRankingCount() {
        return adminSalesService.getTotalSalesRankingCount();
    }

    @GetMapping("/search-ranking")
    @ResponseBody
    public List<SearchRankingDTO> getSearchRanking() {
        return adminSalesService.getSearchRanking(); // 상위 10개 반환
    }

    @GetMapping("/search-total-count")
    @ResponseBody
    public int getTotalSearchRankingCount() {
        return adminSalesService.getTotalSearchRankingCount();
    }
}