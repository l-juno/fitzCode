package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminSalesReportMapper;
import kr.co.fitzcode.common.dto.ChartDataDTO;
import kr.co.fitzcode.common.dto.SalesRankingDTO;
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

        int offset = (page - 1) * 5; // 페이지당 5개 고정
        int totalCount = adminSalesReportMapper.getTotalSalesRankingCount(startDateTime, endDateTime);
        if (page > 2 || (page == 2 && totalCount <= 5)) {
            return new ArrayList<>(); // 2페이지 초과 또는 불필요한 경우 빈 리스트
        }
        return adminSalesReportMapper.getSalesRankingForLast14Days(startDateTime, endDateTime, offset);
    }

    @Override
    public List<SalesRankingDTO> getSalesRankingForLast14Days() {
        return getSalesRankingForLast14Days(1, 5); // 초기 로드 시 첫 페이지 5개
    }

    @Override
    public int getTotalSalesRankingCount() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(13);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return adminSalesReportMapper.getTotalSalesRankingCount(startDateTime, endDateTime);
    }
}