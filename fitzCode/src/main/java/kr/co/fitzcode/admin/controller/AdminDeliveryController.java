package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/deliveries")
@RequiredArgsConstructor
public class AdminDeliveryController {
    private final AdminDeliveryService deliveryService;

    @GetMapping("/deliveries/{orderId}")
    public String manageDeliveries(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "admin/order/manageDelivery";
    }
}