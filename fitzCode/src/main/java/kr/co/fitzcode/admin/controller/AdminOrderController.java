package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminOrderService;
import kr.co.fitzcode.common.dto.AdminOrderDTO;
import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService orderService;

    @GetMapping
    public String getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            Model model) {
        List<AdminOrderDTO> orders = orderService.getOrderList(page, size, status, sortBy);
        int totalCount = orderService.getTotalOrderCount(status);
        int totalPages = orderService.calculateTotalPages(totalCount, size);
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
}