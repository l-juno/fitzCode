package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminOrderMapper;
import kr.co.fitzcode.common.dto.AdminOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
    private final AdminOrderMapper orderMapper;
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_BUTTON_COUNT = 5;

    @Override
    public List<AdminOrderDTO> getOrderList(int page, int size, Integer status, String sortBy) {
        if (page < 1) page = 1;
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        int offset = (page - 1) * size;
        return orderMapper.getOrderList(size, offset, status, sortBy);
    }

    @Override
    public int getTotalOrderCount(Integer status) {
        System.out.println("Status parameter: " + status); // 디버깅 로그
        return orderMapper.getTotalOrderCount(status);
    }

    @Override
    public int calculateTotalPages(int totalCount, int size) {
        if (size != PAGE_SIZE) size = PAGE_SIZE;
        return Math.max(1, (int) Math.ceil((double) totalCount / size));
    }

    @Override
    public int[] getPageRange(int currentPage, int totalPages) {
        int startPage = Math.max(1, currentPage - (PAGE_BUTTON_COUNT / 2));
        int endPage = Math.min(totalPages, startPage + PAGE_BUTTON_COUNT - 1);
        startPage = Math.max(1, endPage - PAGE_BUTTON_COUNT + 1);
        int[] range = new int[endPage - startPage + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = startPage + i;
        }
        return range;
    }
}