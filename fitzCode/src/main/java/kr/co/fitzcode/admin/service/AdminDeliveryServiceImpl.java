package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminDeliveryMapper;
import kr.co.fitzcode.common.dto.DeliveryDTO;
import kr.co.fitzcode.common.enums.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDeliveryServiceImpl implements AdminDeliveryService {
    private final AdminDeliveryMapper deliveryMapper;

    @Override
    public void createDelivery(DeliveryDTO delivery) {
        deliveryMapper.insertDelivery(delivery);
    }

    @Override
    public DeliveryDTO getDeliveryByOrderId(Long orderId) {
        return deliveryMapper.getDeliveryByOrderId(orderId);
    }

    @Override
    public void updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        deliveryMapper.updateDeliveryStatus(deliveryId, status);
    }
}