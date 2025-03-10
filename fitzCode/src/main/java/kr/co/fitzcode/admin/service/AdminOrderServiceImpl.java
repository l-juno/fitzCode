package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminOrderMapper;
import kr.co.fitzcode.common.dto.AdminOrderDTO;
import kr.co.fitzcode.common.dto.AdminOrderDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderMapper orderMapper;

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_BUTTON_COUNT = 5;


    // 주문 목록 조회 (페이지네이션)
    @Override
    public List<AdminOrderDTO> getOrderList(int page, int size, Integer status, String sortBy) {
        if (page < 1) page = 1;
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        int offset = (page - 1) * size;
        return orderMapper.getOrderList(size, offset, status, sortBy);
    }

    // 전체 주문 개수 조회 (필터)
    @Override
    public int getTotalOrderCount(Integer status) {
        return orderMapper.getTotalOrderCount(status);
    }

    // 페이지 크기가 기본 크기와 다르면 기본 크기로 설정
    @Override
    public int calculateTotalPages(int totalCount, int size) {
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        // 전체 페이지 수
        return Math.max(1, (int) Math.ceil((double) totalCount / size));
    }

    // 페이지네이션 시작 페이지
    @Override
    public int[] getPageRange(int currentPage, int totalPages) {
        int startPage = Math.max(1, currentPage - (PAGE_BUTTON_COUNT / 2));
        int endPage = Math.min(totalPages, startPage + PAGE_BUTTON_COUNT - 1);
        startPage = Math.max(1, endPage - PAGE_BUTTON_COUNT + 1);

        // 페이지 범위
        int[] range = new int[endPage - startPage + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = startPage + i;
        }
        return range;
    }

    // 주문 상세 정보 조회
    @Override
    public AdminOrderDetailDTO getOrderDetail(Long orderId) {
        return orderMapper.getOrderDetail(orderId);
    }
}