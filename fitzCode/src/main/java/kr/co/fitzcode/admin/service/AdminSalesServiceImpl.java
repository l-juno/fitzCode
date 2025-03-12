package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminSalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 관리자 판매 현황 서비스 구현체.
 * MyBatis 매퍼를 통해 데이터를 조회.
 */
@Service
@RequiredArgsConstructor
public class AdminSalesServiceImpl implements AdminSalesService {

    private final AdminSalesMapper adminSalesMapper;

    @Override
    public List<String> getDailyLabels(LocalDate startDate, LocalDate endDate) {
        return adminSalesMapper.getDailyLabels(startDate, endDate);
    }

    @Override
    public List<Integer> getDailySalesAmount(LocalDate startDate, LocalDate endDate) {
        return adminSalesMapper.getDailySalesAmount(startDate, endDate);
    }

    @Override
    public List<Integer> getDailySalesQuantity(LocalDate startDate, LocalDate endDate) {
        return adminSalesMapper.getDailySalesQuantity(startDate, endDate);
    }
}