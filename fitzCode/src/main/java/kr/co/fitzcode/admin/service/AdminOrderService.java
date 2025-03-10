package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.AdminOrderDTO;
import kr.co.fitzcode.common.dto.AdminOrderDetailDTO;

import java.util.List;

public interface AdminOrderService {

    // 주문 목록 조회
    List<AdminOrderDTO> getOrderList(int page, int size, Integer status, String sortBy);

    // 특정 상태 총 주문 개수
    int getTotalOrderCount(Integer status);

    // 전체 주문 개수 -> 총 페이지 수 계산
    int calculateTotalPages(int totalCount, int size);

    // 현재 페이지 기준 -> 페이지네이션 버튼 범위 계산
    int[] getPageRange(int currentPage, int totalPages);

    // 주문 상세 정보 조회
    AdminOrderDetailDTO getOrderDetail(Long orderId);
}