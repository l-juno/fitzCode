package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.DeliveryDTO;
import kr.co.fitzcode.common.enums.DeliveryStatus;

public interface AdminDeliveryService {
    void createDelivery(DeliveryDTO delivery);
    DeliveryDTO getDeliveryByOrderId(Long orderId);
    void updateDeliveryStatus(Long deliveryId, DeliveryStatus status);
}