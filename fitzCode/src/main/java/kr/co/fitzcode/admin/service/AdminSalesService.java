package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.ChartDataDTO;
import kr.co.fitzcode.common.dto.SalesRankingDTO;
import kr.co.fitzcode.common.dto.SearchRankingDTO;

import java.util.List;

public interface AdminSalesService {
    ChartDataDTO getSalesDataForLast30Days();
    List<String> getDailyLabels();
    List<SalesRankingDTO> getSalesRankingForLast14Days(int page, int size);
    List<SalesRankingDTO> getSalesRankingForLast14Days();
    int getTotalSalesRankingCount();

    List<SearchRankingDTO> getSearchRanking(); // 검색어 순위 상위 10개 반환
    int getTotalSearchRankingCount(); // 오늘 검색된 고유 키워드 수 반환
}