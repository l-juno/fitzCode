package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.ChartDataDTO;
import kr.co.fitzcode.common.dto.SalesRankingDTO;

import java.util.List;

public interface AdminSalesService {
    ChartDataDTO getSalesDataForLast30Days();
    List<String> getDailyLabels();
    List<SalesRankingDTO> getSalesRankingForLast14Days(int page, int size);
    List<SalesRankingDTO> getSalesRankingForLast14Days();
    int getTotalSalesRankingCount();
}