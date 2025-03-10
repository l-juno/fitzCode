package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminOrderService;
import kr.co.fitzcode.common.dto.AdminOrderDTO;
import kr.co.fitzcode.common.dto.AdminOrderDetailDTO;
import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService orderService;

    // 주문 목록 조회
    @GetMapping
    public String getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            Model model) {
        // 주문 목록
        List<AdminOrderDTO> orders = orderService.getOrderList(page, size, status, sortBy);
        // 전체 주문
        int totalCount = orderService.getTotalOrderCount(status);
        // 페이지 수
        int totalPages = orderService.calculateTotalPages(totalCount, size);
        // 페이지네이션 범위
        int[] pageRange = orderService.getPageRange(page, totalPages);

        model.addAttribute("orders", orders != null ? orders : List.of());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("pageRange", pageRange);

        return "admin/order/orderList";
    }

    // 특정 주문 상세 정보조회
    @GetMapping("/{orderId}")
    public String getOrderDetail(@PathVariable Long orderId, Model model) {
        // 주문 상세 정보 조회
        AdminOrderDetailDTO orderDetail = orderService.getOrderDetail(orderId);

        if (orderDetail == null) {
            log.warn("주문 상세 정보 없ㅇㅁ 주문 ID: {}", orderId);
        } else {
            log.info("주문 ID: {}", orderDetail.getOrderId());
            log.info("총 금액: {}원", orderDetail.getTotalAmount());
            log.info("이름: {}", orderDetail.getUsername());
            log.info("상품 개수: {}개", (orderDetail.getItems() != null ? orderDetail.getItems().size() : 0));
        }

        model.addAttribute("orderDetail", orderDetail != null ? orderDetail : new AdminOrderDetailDTO());

        return "admin/order/orderDetail";
    }
}