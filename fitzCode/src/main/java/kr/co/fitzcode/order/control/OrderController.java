package kr.co.fitzcode.order.control;


import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문내역으로 이동하는 페이지
    @GetMapping("/orderList")
    public String orderList(Model model, HttpSession session){
        session.setAttribute("userId", 2);
        int userId =  (int) session.getAttribute("userId");
        List<HashMap<String, Object>> orderList = orderService.OrderList(userId);
        model.addAttribute("orderList", orderList);
        return "order/orderList";
    }
}
