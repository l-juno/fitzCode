package kr.co.fitzcode.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

    @GetMapping("/order/{orderId}")
    public String order(@PathVariable Long orderId, Model model) {
        return "order/order";
    }

}
