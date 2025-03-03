package kr.co.fitzcode.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {
    // 1대1 문의 관련
    int countByStatus(int status);

    // 1주일간 신규 가입자 수 조회 (날짜 정보 포함)
    List<Map<String, Object>> getWeeklyNewUsersWithDates();

    // 월별 총 매출 조회 (날짜와 금액 정보 포함)
    List<Map<String, Object>> getMonthlyTotalSales();

    // 오늘의 총 주문 수
    int getTodayOrdersCount();

    // 오늘의 총 매출
    Integer getTodayTotalSales();

    // 현재 접속자 수 (WebSocket 기반, 임시값)
    int getCurrentVisitors();

    // 최근 24시간 동안의 취소/반품 요청 수
    int getCancelReturnsCount();

    // 등급별 회원 수 조회
    List<Map<String, Object>> getMemberTierCounts();
}