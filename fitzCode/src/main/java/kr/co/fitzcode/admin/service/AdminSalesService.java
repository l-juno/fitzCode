package kr.co.fitzcode.admin.service;

import java.time.LocalDate;
import java.util.List;

public interface AdminSalesService {

    // 최근 기간의 날짜 레이블 조회
    List<String> getDailyLabels(LocalDate startDate, LocalDate endDate);

    // 최근 기간의 일별 매출액을 조회
    List<Integer> getDailySalesAmount(LocalDate startDate, LocalDate endDate);

    //최근 기간의 일별 판매 수량을 조회
    List<Integer> getDailySalesQuantity(LocalDate startDate, LocalDate endDate);
}