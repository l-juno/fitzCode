package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminSalesReportMapper;
import kr.co.fitzcode.common.dto.ChartDataDTO;
import kr.co.fitzcode.common.dto.SalesRankingDTO;
import kr.co.fitzcode.common.dto.SearchRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminSalesServiceImpl implements AdminSalesService {

    private final AdminSalesReportMapper adminSalesReportMapper;

    @Override
    public ChartDataDTO getSalesDataForLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Map<String, Object>> salesData = adminSalesReportMapper.getDailySalesData(startDateTime, endDateTime);
        ChartDataDTO chartData = new ChartDataDTO();

        for (Map<String, Object> data : salesData) {
            java.sql.Date sqlDate = (java.sql.Date) data.get("sale_date");
            LocalDate saleDate = sqlDate.toLocalDate();

            int totalSales = ((Number) data.get("total_sales")).intValue();
            int salesCount = ((Number) data.get("sales_count")).intValue();

            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, saleDate);
            chartData.getRevenueData().set(dayIndex, totalSales);
            chartData.getVolumeData().set(dayIndex, salesCount);
        }

        return chartData;
    }

    @Override
    public List<String> getDailyLabels() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        List<String> labels = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            labels.add(currentDate.format(formatter));
            currentDate = currentDate.plusDays(1);
        }

        return labels;
    }

    @Override
    public List<SalesRankingDTO> getSalesRankingForLast14Days(int page, int size) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(13);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        int offset = (page - 1) * 5;
        int totalCount = adminSalesReportMapper.getTotalSalesRankingCount(startDateTime, endDateTime);
        if (page > 2 || (page == 2 && totalCount <= 5)) {
            return new ArrayList<>();
        }
        return adminSalesReportMapper.getSalesRankingForLast14Days(startDateTime, endDateTime, offset);
    }

    @Override
    public List<SalesRankingDTO> getSalesRankingForLast14Days() {
        return getSalesRankingForLast14Days(1, 5);
    }

    @Override
    public int getTotalSalesRankingCount() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(13);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return adminSalesReportMapper.getTotalSalesRankingCount(startDateTime, endDateTime);
    }

    @Override
    public List<SearchRankingDTO> getSearchRanking() {
        return adminSalesReportMapper.getSearchRanking();
    }

    @Override
    public int getTotalSearchRankingCount() {
        return adminSalesReportMapper.getTotalSearchRankingCount();
    }
}