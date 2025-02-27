package kr.co.fitzcode.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @GetMapping("/tracking")
    public String deliveryTracking(){
        return "delivery/deliveryTrackingList";
    }

}
