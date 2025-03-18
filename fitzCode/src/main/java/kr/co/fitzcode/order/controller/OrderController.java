package kr.co.fitzcode.order.controller;

import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserOrderDetailDTO;
import kr.co.fitzcode.order.service.OrderService;
import kr.co.fitzcode.order.service.UserOrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserOrderDetailService userOrderDetailService;

    @GetMapping("/order/{orderId}")
    public String order(@PathVariable int orderId, Model model) {

        OrderDTO orderDTO = orderService.getOrderByOrderId(orderId);
        List<UserOrderDetailDTO> list = userOrderDetailService.getOrderDetailByOrderId(orderId);
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("orderDetailList", list);
        log.info("orderDTO: {}", orderDTO);
        log.info("list: {}", list);

        return "order/order";
    }

}
