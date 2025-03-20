package kr.co.fitzcode.order.controller;

import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserOrderDTO;
import kr.co.fitzcode.common.dto.UserOrderDetailDTO;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.order.service.OrderService;
import kr.co.fitzcode.order.service.UserOrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserOrderDetailService userOrderDetailService;

    @GetMapping("/order/{orderId}")
    public String order(@PathVariable int orderId, Model model) {

        UserOrderDTO orderDTO = orderService.getOrderByOrderId(orderId);
        log.info("orderDTO: {}", orderDTO);
        List<UserOrderDetailDTO> list = userOrderDetailService.getOrderDetailByOrderId(orderId);
        log.info("list: {}", list);
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("orderDetailList", list);

        return "order/order";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        int userId = SecurityUtils.getUserId();
        List<UserOrderDetailDTO> list = userOrderDetailService.getOrderDetailByUserId(userId);
        log.info("list::::: {}", list);

        // Group by orderId
        Map<Integer, List<UserOrderDetailDTO>> groupedOrders = list.stream()
                .collect(Collectors.groupingBy(UserOrderDetailDTO::getOrderId));
        log.info("groupedOrders::::: {}", groupedOrders);

        model.addAttribute("groupedOrders", groupedOrders);
        return "order/allOrders";
    }




}
