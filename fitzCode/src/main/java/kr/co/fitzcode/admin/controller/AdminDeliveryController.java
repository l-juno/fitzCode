package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.AdminDeliveryService;
import kr.co.fitzcode.common.dto.DeliveryDTO;
import kr.co.fitzcode.common.enums.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/deliveries")
@RequiredArgsConstructor
public class AdminDeliveryController {
    private final AdminDeliveryService deliveryService;

    // 배송 정보 조회
    @GetMapping("/{orderId}")
    public String getDelivery(@PathVariable Long orderId, Model model) {
        DeliveryDTO delivery = deliveryService.getDeliveryByOrderId(orderId);
        if (delivery == null) {
            delivery = new DeliveryDTO();
            delivery.setOrderId(orderId);
            delivery.setDeliveryStatus(DeliveryStatus.PENDING);
        }
        model.addAttribute("delivery", delivery);
        return "admin/delivery/deliveryDetail";
    }

    // 배송 정보 생성/수정
    @PostMapping("/{orderId}")
    public String createOrUpdateDelivery(@PathVariable Long orderId,
                                         @RequestParam String trackingNumber,
                                         @RequestParam DeliveryStatus deliveryStatus,
                                         @RequestParam(required = false) LocalDateTime shippedAt,
                                         @RequestParam(required = false) LocalDateTime deliveredAt) {
        DeliveryDTO delivery = deliveryService.getDeliveryByOrderId(orderId);
        if (delivery == null) {
            delivery = new DeliveryDTO();
            delivery.setOrderId(orderId);
            delivery.setTrackingNumber(trackingNumber);
            delivery.setDeliveryStatus(deliveryStatus);
            delivery.setShippedAt(shippedAt);
            delivery.setDeliveredAt(deliveredAt);
            deliveryService.createDelivery(delivery);
        } else {
            delivery.setTrackingNumber(trackingNumber);
            delivery.setDeliveryStatus(deliveryStatus);
            delivery.setShippedAt(shippedAt);
            delivery.setDeliveredAt(deliveredAt);
            deliveryService.updateDeliveryStatus(delivery.getDeliveryId(), deliveryStatus);
        }
        return "redirect:/admin/deliveries/" + orderId;
    }

    // 배송 상태 업데이트
    @PostMapping("/{deliveryId}/status")
    public String updateDeliveryStatus(@PathVariable Long deliveryId, @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatus(deliveryId, status);
        DeliveryDTO delivery = deliveryService.getDeliveryByOrderId(deliveryId);
        return "redirect:/admin/deliveries/" + (delivery != null ? delivery.getOrderId() : deliveryId);
    }

    // 배송 취소 (ORDERS 테이블의 order_status를 업데이트)
    @PostMapping("/{orderId}/cancel")
    public String cancelDelivery(@PathVariable Long orderId) {
        // ORDERS 테이블의 order_status를 4(CANCELLED)로 업데이트하는 로직 추가 필요
        // 예: OrderService를 통해 처리
        // orderService.updateOrderStatus(orderId, 4);
        return "redirect:/admin/deliveries/" + orderId;
    }
}