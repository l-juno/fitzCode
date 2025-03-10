package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.AdminOrderDTO;

import java.util.List;

public interface AdminOrderService {
    List<AdminOrderDTO> getOrderList(int page, int size, Integer status, String sortBy);
    int getTotalOrderCount(Integer status);
    int calculateTotalPages(int totalCount, int size);
    int[] getPageRange(int currentPage, int totalPages);
}